package com.vetlink.pet.tracker.iam.domain.services;

import com.vetlink.pet.tracker.iam.domain.model.entities.Role;
import com.vetlink.pet.tracker.iam.domain.model.queries.GetAllRolesQuery;
import com.vetlink.pet.tracker.iam.domain.model.queries.GetRoleByNameQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
    Optional<Role> handle(GetRoleByNameQuery query);
}
