package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.Coordinate;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.GeoFenceStatuses;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

import java.util.List;

public record CreateGeoFenceCommand(PetTrackerDeviceRecordId petTrackerDeviceRecordId, String name, GeoFenceStatuses geoFenceStatus, List<Coordinate> coordinates) {
}
