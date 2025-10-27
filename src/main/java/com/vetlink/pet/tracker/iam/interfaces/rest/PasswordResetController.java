package com.vetlink.pet.tracker.iam.interfaces.rest;

import com.vetlink.pet.tracker.iam.domain.services.PasswordResetCommandService;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.MessageResource;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.RequestPasswordResetResource;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.ResetPasswordResource;
import com.vetlink.pet.tracker.iam.interfaces.rest.transform.RequestPasswordResetCommandFromResourceAssembler;
import com.vetlink.pet.tracker.iam.interfaces.rest.transform.ResetPasswordCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/password-reset", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Password Reset", description = "Password Reset Management Endpoints")
public class PasswordResetController {

    private final PasswordResetCommandService passwordResetCommandService;

    public PasswordResetController(PasswordResetCommandService passwordResetCommandService) {
        this.passwordResetCommandService = passwordResetCommandService;
    }

    @PostMapping("/request")
    @Operation(summary = "Request password reset", description = "Send a password reset email to the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset email sent if the email exists"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<MessageResource> requestPasswordReset(@RequestBody RequestPasswordResetResource resource) {
        try {
            var command = RequestPasswordResetCommandFromResourceAssembler.toCommandFromResource(resource);
            passwordResetCommandService.handle(command);
            
            // Mensaje genérico por seguridad
            return ResponseEntity.ok(
                new MessageResource("If the email exists, a password reset link has been sent.")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResource("An error occurred while processing your request."));
        }
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset password", description = "Reset user password using the token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully reset"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    public ResponseEntity<MessageResource> resetPassword(@RequestBody ResetPasswordResource resource) {
        try {
            var command = ResetPasswordCommandFromResourceAssembler.toCommandFromResource(resource);
            passwordResetCommandService.handle(command);
            
            return ResponseEntity.ok(
                new MessageResource("Password has been successfully reset.")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResource(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResource("An error occurred while resetting your password."));
        }
    }
}
