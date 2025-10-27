package com.vetlink.pet.tracker.iam.infrastructure.email;

import com.vetlink.pet.tracker.iam.application.internal.outboundservices.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String username, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Pet Tracker - Solicitud de restablecimiento de contraseña");
            
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
            
            String emailBody = String.format(
                "Hola %s,\n\n" +
                "Has solicitado restablecer tu contraseña para tu cuenta de Pet Tracker.\n\n" +
                "Por favor, haz clic en el siguiente enlace para restablecer tu contraseña:\n%s\n\n" +
                "Este enlace expirará en 30 minutos.\n\n" +
                "Si no realizaste esta solicitud, puedes ignorar este correo.\n\n" +
                "Saludos cordiales,\n" +
                "Equipo de Pet Tracker",
                username, resetLink
            );
            
            message.setText(emailBody);
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
