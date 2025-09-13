package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.GeoFenceStatuses;
import com.vetlink.pet.tracker.shared.domain.model.valueobjects.Pair;

import java.util.List;

public record UpdateGeoFenceResource(String name, String geoFenceStatus, java.util.List<Pair<Float, Float>> coordinates) {
}