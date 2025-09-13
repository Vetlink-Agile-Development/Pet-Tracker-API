package com.vetlink.pet.tracker.monitoring.interfaces.websocket.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.HealthMeasure;
import com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource.HealthMeasureResource;

public class HealthMeasureResourceFromEntityAssembler {
    public static HealthMeasureResource toResourceFromEntity(HealthMeasure healthMeasure){
        return new HealthMeasureResource(
                healthMeasure.getBpm().bpm(),
                healthMeasure.getSpo2().spo2()
        );
    }
}
