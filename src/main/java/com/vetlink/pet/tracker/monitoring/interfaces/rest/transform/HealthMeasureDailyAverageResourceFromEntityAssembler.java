package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.HealthMeasureDailyAverage;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.HealthMeasureDailyAverageResource;

public class HealthMeasureDailyAverageResourceFromEntityAssembler {
    public static HealthMeasureDailyAverageResource toResourceFromEntity(HealthMeasureDailyAverage healthMeasureDailyAverage) {
        return new HealthMeasureDailyAverageResource(
                healthMeasureDailyAverage.getDate(),
                healthMeasureDailyAverage.getAvgBpm(),
                healthMeasureDailyAverage.getAvgSpo2()
        );
    }
}
