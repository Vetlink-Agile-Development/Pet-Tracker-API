package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.HealthMeasure;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateHealthMeasureCommand;

import java.util.Optional;

public interface HealthMeasureCommandService {
    Optional<HealthMeasure> handle(CreateHealthMeasureCommand command);
}
