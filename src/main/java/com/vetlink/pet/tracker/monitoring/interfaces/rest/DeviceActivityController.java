package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllActivitiesByPetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllActivitiesByPetTrackerDeviceRecordIdAndActivityType;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityType;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.services.ActivityQueryService;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.ActivityResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.ActivityResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/devices/{deviceRecordId}/activities", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Devices")
public class DeviceActivityController {
    private final ActivityQueryService activityQueryService;

    public DeviceActivityController(ActivityQueryService activityQueryService) {
        this.activityQueryService = activityQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityResource>> getActivitiesFromDeviceRecordId(@PathVariable String deviceRecordId, @RequestParam(required = false) String activityType){
        var petTrackerDeviceRecordId = new PetTrackerDeviceRecordId(deviceRecordId);
        if (activityType != null) {
            var activityTypeEnum = ActivityType.valueOf(activityType);
            var query = new GetAllActivitiesByPetTrackerDeviceRecordIdAndActivityType(petTrackerDeviceRecordId, activityTypeEnum);
            var activities = activityQueryService.handle(query);
            var activitiesResource = activities.stream().map(ActivityResourceFromEntityAssembler::toResourceFromEntity).toList();
            return ResponseEntity.ok(activitiesResource);
        }
        else {
            var query = new GetAllActivitiesByPetTrackerDeviceRecordId(petTrackerDeviceRecordId);
            var activities = activityQueryService.handle(query);
            var activitiesResource = activities.stream().map(ActivityResourceFromEntityAssembler::toResourceFromEntity).toList();
            return ResponseEntity.ok(activitiesResource);
        }
    }

}
