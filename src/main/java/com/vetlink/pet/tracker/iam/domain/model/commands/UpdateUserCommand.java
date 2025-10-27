package com.vetlink.pet.tracker.iam.domain.model.commands;

import com.vetlink.pet.tracker.iam.domain.model.entities.Role;

import java.util.List;

public record UpdateUserCommand(Long userId, String email, String firstName, String lastName, String password, List<Role> roles) {
}
