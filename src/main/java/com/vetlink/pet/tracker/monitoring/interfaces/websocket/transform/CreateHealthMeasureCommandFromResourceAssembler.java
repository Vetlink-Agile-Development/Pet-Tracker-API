package com.vetlink.pet.tracker.monitoring.interfaces.websocket.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateHealthMeasureCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ApiKey;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.BeatsPerMinute;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.SaturationOfPeripheralOxygen;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource.CreateHealthMeasureResource;

public class CreateHealthMeasureCommandFromResourceAssembler {
    public static CreateHealthMeasureCommand toCommandFromResource(String apiKey, CreateHealthMeasureResource resource) {
        return new CreateHealthMeasureCommand(
                new BeatsPerMinute(resource.bpm()),
                new SaturationOfPeripheralOxygen(resource.spo2()),
                new PetTrackerDeviceRecordId(resource.petTrackerDeviceRecordId()),
                new ApiKey(apiKey)
        );
    }
}
