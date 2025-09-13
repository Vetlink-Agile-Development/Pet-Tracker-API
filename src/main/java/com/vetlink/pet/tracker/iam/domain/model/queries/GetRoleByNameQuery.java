package com.vetlink.pet.tracker.iam.domain.model.queries;

import com.vetlink.pet.tracker.iam.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles name) {
}
