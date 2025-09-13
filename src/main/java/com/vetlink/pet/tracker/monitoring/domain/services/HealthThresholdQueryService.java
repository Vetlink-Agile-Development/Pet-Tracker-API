package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetHealthThresholdsByPetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.HealthThresholds;

public interface HealthThresholdQueryService {
    HealthThresholds handle(GetHealthThresholdsByPetTrackerDeviceRecordId query);
}
