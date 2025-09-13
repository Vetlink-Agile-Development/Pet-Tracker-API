package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

public record UpdateHealthThresholdsCommand(
    PetTrackerDeviceRecordId petTrackerDeviceRecordId,
    int minBpm,
    int maxBpm,
    int minSpO2,
    int maxSpO2
) {
}
