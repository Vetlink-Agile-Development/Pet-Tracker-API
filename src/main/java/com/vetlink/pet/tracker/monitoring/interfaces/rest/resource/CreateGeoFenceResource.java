package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

import com.vetlink.pet.tracker.shared.domain.model.valueobjects.Pair;

import java.util.List;

public record CreateGeoFenceResource(String name, String geoFenceStatus, List<Pair<Float, Float>> coordinates, String petTrackerDeviceRecordId) {
}
