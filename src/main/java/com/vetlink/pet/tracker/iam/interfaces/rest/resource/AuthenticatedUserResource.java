package com.vetlink.pet.tracker.iam.interfaces.rest.resource;

public record AuthenticatedUserResource(Long id, String username, String token) {
}
