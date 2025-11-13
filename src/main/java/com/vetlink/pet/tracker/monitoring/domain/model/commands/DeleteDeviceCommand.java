package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

public record DeleteDeviceCommand(PetTrackerDeviceRecordId petTrackerDeviceRecordId) {
}
