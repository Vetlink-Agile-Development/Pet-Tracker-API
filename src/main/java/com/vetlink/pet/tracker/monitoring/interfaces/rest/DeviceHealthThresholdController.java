package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetDeviceByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceCommandService;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceQueryService;
import com.vetlink.pet.tracker.monitoring.domain.services.HealthThresholdCommandService;
import com.vetlink.pet.tracker.monitoring.domain.services.HealthThresholdQueryService;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.DeviceHealthThresholdsResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.UpdateDeviceHealthThresholdResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.DeviceHealthThresholdsResourceFromEntityAssembler;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.UpdateHealthThresholdCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Devices")
public class DeviceHealthThresholdController {

    private final HealthThresholdCommandService healthThresholdCommandService;
    private final HealthThresholdQueryService healthThresholdQueryService;
    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DeviceHealthThresholdController(HealthThresholdCommandService healthThresholdCommandService, HealthThresholdQueryService healthThresholdQueryService, DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.healthThresholdCommandService = healthThresholdCommandService;
        this.healthThresholdQueryService = healthThresholdQueryService;
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @PutMapping("/{deviceRecordId}/health-thresholds")
    public ResponseEntity<DeviceHealthThresholdsResource> updateDeviceHealthThresholdsByDeviceRecordId(@PathVariable String deviceRecordId, @RequestBody UpdateDeviceHealthThresholdResource updateDeviceHealthThresholdResource) {
        var updateHealthThresholdsCommand = UpdateHealthThresholdCommandFromResourceAssembler.toCommandFromResource(deviceRecordId, updateDeviceHealthThresholdResource);
        var device = deviceCommandService.handle(updateHealthThresholdsCommand);
        if (device.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var deviceHealthThresholdsResource = DeviceHealthThresholdsResourceFromEntityAssembler.toResourceFromEntity(device.get());
        healthThresholdCommandService.handle(deviceHealthThresholdsResource);
        return ResponseEntity.ok(deviceHealthThresholdsResource);
    }

    @GetMapping("/{deviceRecordId}/health-thresholds")
    public ResponseEntity<DeviceHealthThresholdsResource> getDeviceHealthThresholdsByDeviceRecordId(@PathVariable String deviceRecordId) {
        var petTrackerDeviceRecordId = new PetTrackerDeviceRecordId(deviceRecordId);
        var query = new GetDeviceByPetTrackerDeviceRecordIdQuery(petTrackerDeviceRecordId);
        var device = deviceQueryService.handle(query);
        var deviceHealthThresholdsResource = DeviceHealthThresholdsResourceFromEntityAssembler.toResourceFromEntity(device.get());
        return ResponseEntity.ok(deviceHealthThresholdsResource);
    }

}
