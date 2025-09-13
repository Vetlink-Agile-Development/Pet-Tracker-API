package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.RegisterDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.RegisterDeviceResource;

public class RegisterDeviceCommandFromResourceAssembler {
    public static RegisterDeviceCommand toCommandFromResource(RegisterDeviceResource resource) {
        return new RegisterDeviceCommand(
                new PetTrackerDeviceRecordId(resource.petTrackerDeviceRecordId())
        );
    }
}
