package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetDeviceByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceCommandService;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceQueryService;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.*;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "Devices Management Endpoints")
public class DevicesController {
    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DevicesController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @PostMapping("/assign")
    public ResponseEntity<DeviceResource> assignDevice(@RequestBody AssignDeviceResource createDeviceResource){
        var assignDeviceCommand = AssignDeviceCommandFromResourceAssembler.toCommandFromResource(createDeviceResource);
        var device = deviceCommandService.handle(assignDeviceCommand);
        if (device.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(device.get());
        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiKeyResource> registerDevice(@RequestBody RegisterDeviceResource registerDeviceResource) {
        var registerDeviceCommand = RegisterDeviceCommandFromResourceAssembler.toCommandFromResource(registerDeviceResource);
        var apiKey = deviceCommandService.handle(registerDeviceCommand);
        if (apiKey.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var apiKeyResource = new ApiKeyResource(apiKey.get());
        return new ResponseEntity<>(apiKeyResource, HttpStatus.CREATED);
    }

    @GetMapping("/{deviceRecordId}")
    public ResponseEntity<DeviceResource> getDeviceByDeviceRecordId(@PathVariable String deviceRecordId) {
        var petTrackerDeviceRecordId = new PetTrackerDeviceRecordId(deviceRecordId);
        var getDeviceByPetTrackerDeviceRecordIdQuery = new GetDeviceByPetTrackerDeviceRecordIdQuery(petTrackerDeviceRecordId);
        var device = deviceQueryService.handle(getDeviceByPetTrackerDeviceRecordIdQuery);
        if (device.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(device.get());
        return ResponseEntity.ok(deviceResource);
    }

    @PutMapping("/{deviceRecordId}")
    public ResponseEntity<DeviceResource> updateDeviceByDeviceRecordId(@PathVariable String deviceRecordId, @RequestBody UpdateDeviceResource updateDeviceResource) {
        var updateDeviceCommand = UpdateDeviceCommandFromResourceAssembler.toCommandFromResource(deviceRecordId, updateDeviceResource);
        var device = deviceCommandService.handle(updateDeviceCommand);
        if (device.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(device.get());
        return ResponseEntity.ok(deviceResource);
    }
}
