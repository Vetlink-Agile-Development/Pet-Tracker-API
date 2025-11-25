package com.vetlink.pet.tracker.monitoring.domain.services;
import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.GeoFence;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateGeoFenceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.DeleteGeoFenceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateGeoFenceCommand;

import java.util.Optional;

public interface GeoFenceCommandService {
    Optional<GeoFence> handle(CreateGeoFenceCommand command);
    Optional<GeoFence> handle(UpdateGeoFenceCommand command);
    boolean handle(DeleteGeoFenceCommand command);
}
