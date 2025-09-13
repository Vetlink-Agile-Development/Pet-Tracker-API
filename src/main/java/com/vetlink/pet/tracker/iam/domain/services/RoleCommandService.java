package com.vetlink.pet.tracker.iam.domain.services;

import com.vetlink.pet.tracker.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
