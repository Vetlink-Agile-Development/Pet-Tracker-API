package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.GeoFence;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllGeoFencesByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetDeviceByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetGeoFenceByIdQuery;

import java.util.List;
import java.util.Optional;

public interface GeoFenceQueryService {
    List<GeoFence> handle(GetAllGeoFencesByPetTrackerDeviceRecordIdQuery query);
    Optional<GeoFence> handle(GetGeoFenceByIdQuery query);
}
