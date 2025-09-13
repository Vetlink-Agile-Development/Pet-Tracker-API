package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.Coordinate;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.GeoFenceStatuses;

import java.util.List;

public record UpdateGeoFenceCommand(Long geoFenceId, String name, GeoFenceStatuses geoFenceStatus, List<Coordinate> coordinates) {
}
