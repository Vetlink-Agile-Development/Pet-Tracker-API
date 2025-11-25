package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.UnassignDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.UnassignDeviceResource;

public class UnassignDeviceCommandFromResourceAssembler {
    public static UnassignDeviceCommand toCommandFromResource(UnassignDeviceResource resource){
        return new UnassignDeviceCommand(
                new PetTrackerDeviceRecordId(resource.petTrackerDeviceRecordId()),
                resource.userId()
        );
    }
}
