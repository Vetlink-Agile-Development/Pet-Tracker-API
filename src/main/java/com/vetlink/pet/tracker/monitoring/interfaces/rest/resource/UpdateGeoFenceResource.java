package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

import com.vetlink.pet.tracker.shared.domain.model.valueobjects.Pair;


public record UpdateGeoFenceResource(String name, String geoFenceStatus, java.util.List<Pair<Float, Float>> coordinates) {
}