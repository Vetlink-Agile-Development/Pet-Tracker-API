package com.vetlink.pet.tracker.iam.interfaces.rest.resource;

import java.util.List;

public record UpdateUserResource(String email, String firstName, String lastName, String password, List<String> roles) {
}
