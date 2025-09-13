package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.DeviceHealthThresholdsResource;

public interface HealthThresholdCommandService {
    void handle(DeviceHealthThresholdsResource resource);
}
