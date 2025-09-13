package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.DeviceCareModes;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.DeviceStatuses;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.UpdateDeviceResource;

public class UpdateDeviceCommandFromResourceAssembler {
    public static UpdateDeviceCommand toCommandFromResource(String deviceRecordId, UpdateDeviceResource resource){
        return new UpdateDeviceCommand(
                new PetTrackerDeviceRecordId(deviceRecordId),
                resource.bearer(),
                resource.deviceNickname(),
                DeviceCareModes.valueOf(resource.deviceCareModes()),
                DeviceStatuses.valueOf(resource.deviceStatuses())
        );
    }
}
