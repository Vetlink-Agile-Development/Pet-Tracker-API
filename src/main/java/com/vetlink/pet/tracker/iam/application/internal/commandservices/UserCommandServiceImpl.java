package com.vetlink.pet.tracker.iam.application.internal.commandservices;

import com.vetlink.pet.tracker.iam.application.internal.outboundservices.hashing.HashingService;
import com.vetlink.pet.tracker.iam.application.internal.outboundservices.tokens.TokenService;
import com.vetlink.pet.tracker.iam.domain.model.aggregates.User;
import com.vetlink.pet.tracker.iam.domain.model.commands.SignInCommand;
import com.vetlink.pet.tracker.iam.domain.model.commands.SignUpCommand;
import com.vetlink.pet.tracker.iam.domain.model.commands.UpdateUserCommand;
import com.vetlink.pet.tracker.iam.domain.model.valueobjects.Roles;
import com.vetlink.pet.tracker.iam.domain.services.UserCommandService;
import com.vetlink.pet.tracker.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.vetlink.pet.tracker.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new RuntimeException("Username already exists");
        }
        var roles = command.roles();
        if (roles.isEmpty()){
            var role = roleRepository.findByName(Roles.ROLE_USER);
            roles.add(role.get());
        }
        roles = command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found"))).toList();
        var user = new User(command.username(), command.email(), command.firstName(), command.lastName(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());
        if (user.isEmpty()) throw new RuntimeException("User not found");
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            throw new RuntimeException("Invalid password");
        var currentUser = user.get();
        var token = tokenService.generateToken(currentUser.getUsername());
        return Optional.of(ImmutablePair.of(currentUser, token));
    }

    @Override
    public Optional<User> handle(UpdateUserCommand command) {
        var user = userRepository.findById(command.userId());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        
        var roles = command.roles();
        if (roles != null && !roles.isEmpty()) {
            roles = command.roles().stream()
                    .map(role -> roleRepository.findByName(role.getName())
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found")))
                    .toList();
        }
        
        var password = command.password();
        if (password != null && !password.isEmpty()) {
            password = hashingService.encode(password);
        }
        
        user.get().updateUser(command.email(), command.firstName(), command.lastName(), password, roles);
        userRepository.save(user.get());
        return user;
    }
}
