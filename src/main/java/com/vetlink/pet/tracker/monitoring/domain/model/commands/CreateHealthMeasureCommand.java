package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ApiKey;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.BeatsPerMinute;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.SaturationOfPeripheralOxygen;

public record CreateHealthMeasureCommand(BeatsPerMinute bpm, SaturationOfPeripheralOxygen spo2, PetTrackerDeviceRecordId petTrackerDeviceRecordId, ApiKey apiKey) {
}
