package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.aggregates.User;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User entity, String token){
        return new AuthenticatedUserResource(entity.getId(), entity.getUsername(), token);
    }
}
