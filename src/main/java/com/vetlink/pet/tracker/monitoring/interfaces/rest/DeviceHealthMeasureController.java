package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.services.HealthMeasureQueryService;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.HealthMeasureDailyAverageResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.HealthMeasureDailyAverageResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/devices/{deviceRecordId}/health-measures-monthly-summary", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Devices")
public class DeviceHealthMeasureController {
    private final HealthMeasureQueryService healthMeasureQueryService;

    public DeviceHealthMeasureController(HealthMeasureQueryService healthMeasureQueryService) {
        this.healthMeasureQueryService = healthMeasureQueryService;
    }

    @GetMapping
    public ResponseEntity<List<HealthMeasureDailyAverageResource>> getHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordId(@PathVariable String deviceRecordId){
        var petTrackerDeviceRecordId = new PetTrackerDeviceRecordId(deviceRecordId);
        var getHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery = new GetHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery(petTrackerDeviceRecordId);
        var healthMeasureDailyAverages = healthMeasureQueryService.handle(getHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery);
        var healthMeasureDailyAveragesResource = healthMeasureDailyAverages.stream().map(HealthMeasureDailyAverageResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(healthMeasureDailyAveragesResource);
    }
}
