package com.vetlink.pet.tracker.iam.interfaces.rest.resource;

public record ResetPasswordResource(String token, String newPassword) {
}
