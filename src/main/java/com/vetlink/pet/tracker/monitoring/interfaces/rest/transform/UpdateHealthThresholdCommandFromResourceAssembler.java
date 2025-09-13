package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateHealthThresholdsCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.UpdateDeviceHealthThresholdResource;

public class UpdateHealthThresholdCommandFromResourceAssembler {
    public static UpdateHealthThresholdsCommand toCommandFromResource(String deviceRecordId, UpdateDeviceHealthThresholdResource resource){
        return new UpdateHealthThresholdsCommand(
                new PetTrackerDeviceRecordId(deviceRecordId),
                resource.minBpm(),
                resource.maxBpm(),
                resource.minSpO2(),
                resource.maxSpO2()
        );
    }
}
