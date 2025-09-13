package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.HealthMeasure;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllHealthMeasuresByDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.HealthMeasureDailyAverage;

import java.util.List;

public interface HealthMeasureQueryService {
    List<HealthMeasure> handle(GetAllHealthMeasuresByDeviceRecordIdQuery query);
    List<HealthMeasureDailyAverage> handle(GetHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery query);
}
