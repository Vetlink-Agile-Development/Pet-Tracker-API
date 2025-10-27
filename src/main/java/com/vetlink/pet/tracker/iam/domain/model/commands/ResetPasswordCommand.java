package com.vetlink.pet.tracker.iam.domain.model.commands;

public record ResetPasswordCommand(String token, String newPassword) {
}
