package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.DeviceCareModes;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.DeviceStatuses;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

public record UpdateDeviceCommand(PetTrackerDeviceRecordId petTrackerDeviceRecordId, String bearer, String deviceNickname, DeviceCareModes deviceCareModes, DeviceStatuses deviceStatuses) {
}
