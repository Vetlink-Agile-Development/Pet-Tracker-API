package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.GeoFence;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateGeoFenceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.GeoFenceStatuses;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.CreateGeoFenceResource;

public class CreateGeoFenceCommandFromResourceAssembler {
    public static CreateGeoFenceCommand toCommandFromResource(CreateGeoFenceResource resource){
        return new CreateGeoFenceCommand(
                new PetTrackerDeviceRecordId(resource.petTrackerDeviceRecordId()),
                resource.name(),
                GeoFenceStatuses.valueOf(resource.geoFenceStatus()),
                resource.coordinates().stream().map(GeoFence::toCoordinateFromLatitudeAndLongitudePair).toList()
        );
    }
}
