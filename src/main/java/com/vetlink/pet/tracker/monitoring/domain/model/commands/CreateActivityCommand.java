package com.vetlink.pet.tracker.monitoring.domain.model.commands;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityEventName;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityType;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

public record CreateActivityCommand(PetTrackerDeviceRecordId petTrackerDeviceRecordId, ActivityEventName activityEventName, ActivityType activityType) {
}
