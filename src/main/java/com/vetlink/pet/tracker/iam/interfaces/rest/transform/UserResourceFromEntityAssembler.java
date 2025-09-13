package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.aggregates.User;
import com.vetlink.pet.tracker.iam.domain.model.entities.Role;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        var roles = entity.getRoles().stream().map(Role::getStringName).toList();
        return new UserResource(entity.getId(), entity.getUsername(), entity.getEmail().address(), entity.getPersonName().firstName(), entity.getPersonName().lastName(), roles);
    }
}
