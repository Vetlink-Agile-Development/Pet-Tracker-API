package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

public record AssignDeviceCommand(PetTrackerDeviceRecordId petTrackerDeviceRecordId, Long userId) {
}
