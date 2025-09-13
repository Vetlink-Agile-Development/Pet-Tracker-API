package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Activity;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateActivityCommand;

import java.util.Optional;

public interface ActivityCommandService {
    Optional<Activity> handle(CreateActivityCommand command);
}
