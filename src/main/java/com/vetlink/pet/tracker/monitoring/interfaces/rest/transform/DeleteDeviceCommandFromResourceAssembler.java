package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.DeleteDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;

public class DeleteDeviceCommandFromResourceAssembler {
    public static DeleteDeviceCommand toCommandFromResource(String deviceRecordId){
        return new DeleteDeviceCommand(new PetTrackerDeviceRecordId(deviceRecordId));
    }
}
