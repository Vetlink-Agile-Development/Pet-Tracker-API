package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.entities.Role;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource roleResourceFromEntity(Role entity) {
        return new RoleResource(entity.getId(), entity.getStringName());
    }
}
