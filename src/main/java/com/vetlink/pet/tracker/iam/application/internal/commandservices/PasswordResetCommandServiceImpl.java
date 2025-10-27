package com.vetlink.pet.tracker.iam.application.internal.commandservices;

import com.vetlink.pet.tracker.iam.application.internal.outboundservices.email.EmailService;
import com.vetlink.pet.tracker.iam.application.internal.outboundservices.hashing.HashingService;
import com.vetlink.pet.tracker.iam.domain.model.aggregates.PasswordResetToken;
import com.vetlink.pet.tracker.iam.domain.model.commands.RequestPasswordResetCommand;
import com.vetlink.pet.tracker.iam.domain.model.commands.ResetPasswordCommand;
import com.vetlink.pet.tracker.iam.domain.model.valueobjects.EmailAddress;
import com.vetlink.pet.tracker.iam.domain.services.PasswordResetCommandService;
import com.vetlink.pet.tracker.iam.infrastructure.persistence.jpa.repositories.PasswordResetTokenRepository;
import com.vetlink.pet.tracker.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordResetCommandServiceImpl implements PasswordResetCommandService {

    private static final int TOKEN_EXPIRATION_MINUTES = 30;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final HashingService hashingService;

    public PasswordResetCommandServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService,
            HashingService hashingService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.hashingService = hashingService;
    }

    @Override
    @Transactional
    public void handle(RequestPasswordResetCommand command) {
        // Buscar usuario por email
        var emailAddress = new EmailAddress(command.email());
        var userOptional = userRepository.findByEmail(emailAddress);

        // Por seguridad, no revelar si el email existe o no
        if (userOptional.isEmpty()) {
            // Simplemente retornar sin error para no revelar información
            return;
        }

        var user = userOptional.get();

        // Invalidar tokens anteriores del usuario
        passwordResetTokenRepository.deleteByUser(user);

        // Generar nuevo token único
        String token = UUID.randomUUID().toString();

        // Crear y guardar el token de reseteo
        var passwordResetToken = new PasswordResetToken(token, user, TOKEN_EXPIRATION_MINUTES);
        passwordResetTokenRepository.save(passwordResetToken);

        // Enviar email con el token
        try {
            emailService.sendPasswordResetEmail(command.email(), user.getUsername(), token);
        } catch (Exception e) {
            // Log el error pero no fallar la operación por seguridad
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handle(ResetPasswordCommand command) {
        // Buscar el token
        var tokenOptional = passwordResetTokenRepository.findByToken(command.token());

        if (tokenOptional.isEmpty()) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        var resetToken = tokenOptional.get();

        // Validar el token
        if (!resetToken.isValid()) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        // Obtener el usuario
        var user = resetToken.getUser();

        // Actualizar la contraseña
        var hashedPassword = hashingService.encode(command.newPassword());
        user.updateUser(null, null, null, hashedPassword, null);

        // Marcar el token como usado
        resetToken.markAsUsed();

        // Guardar cambios
        userRepository.save(user);
        passwordResetTokenRepository.save(resetToken);
    }
}
