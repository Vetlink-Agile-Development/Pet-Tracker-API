package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.commands.UpdateUserCommand;
import com.vetlink.pet.tracker.iam.domain.model.entities.Role;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.UpdateUserResource;

import java.util.ArrayList;

public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(Long userId, UpdateUserResource resource){
        var roles = resource.roles() != null
                ? resource.roles().stream().map(Role::toRoleFromName).toList()
                : new ArrayList<Role>();
        return new UpdateUserCommand(userId, resource.email(), resource.firstName(), resource.lastName(), resource.password(), roles);
    }
}
