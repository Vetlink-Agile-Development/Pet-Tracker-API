package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.AssignDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.AssignDeviceResource;

public class AssignDeviceCommandFromResourceAssembler {
    public static AssignDeviceCommand toCommandFromResource(AssignDeviceResource resource){
        return new AssignDeviceCommand(
                new PetTrackerDeviceRecordId(resource.petTrackerDeviceRecordId()),
                resource.userId()
        );
    }
}
