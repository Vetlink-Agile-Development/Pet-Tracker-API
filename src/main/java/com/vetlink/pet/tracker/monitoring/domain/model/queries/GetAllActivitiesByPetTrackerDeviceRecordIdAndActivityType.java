package com.vetlink.pet.tracker.monitoring.domain.model.queries;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityType;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

public record GetAllActivitiesByPetTrackerDeviceRecordIdAndActivityType(
        PetTrackerDeviceRecordId petTrackerDeviceRecordId, ActivityType activityType) {
}
