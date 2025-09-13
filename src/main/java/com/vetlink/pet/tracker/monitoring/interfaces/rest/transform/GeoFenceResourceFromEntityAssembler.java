package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.GeoFence;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.GeoFenceResource;

public class GeoFenceResourceFromEntityAssembler {
    public static GeoFenceResource toResourceFromEntity(GeoFence geoFence){
        return new GeoFenceResource(
                geoFence.getId(),
                geoFence.getName(),
                geoFence.getGeoFenceStatus().toString(),
                geoFence.getCoordinates().stream().map(GeoFence::toLatitudeAndLongitudePairFromCoordinate).toList(),
                geoFence.getDeviceRecordId()
        );
    }
}
