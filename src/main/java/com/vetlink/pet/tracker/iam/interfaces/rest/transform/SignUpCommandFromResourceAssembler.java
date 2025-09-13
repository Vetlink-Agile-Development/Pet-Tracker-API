package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.commands.SignUpCommand;
import com.vetlink.pet.tracker.iam.domain.model.entities.Role;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.SignUpResource;

import java.util.ArrayList;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource){
        var roles = resource.roles() !=null
                ? resource.roles().stream().map(Role::toRoleFromName).toList()
                : new ArrayList<Role>();
        return new SignUpCommand(resource.username(), resource.email(), resource.firstName(), resource.lastName(), resource.password(), roles);
    }
}
