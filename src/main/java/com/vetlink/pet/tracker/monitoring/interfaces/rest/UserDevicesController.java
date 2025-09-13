package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllDevicesByUserIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceQueryService;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.DeviceResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users")
public class UserDevicesController {

    private final DeviceQueryService deviceQueryService;

    public UserDevicesController(DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceResource>> getDevicesForUserWithUsername(@PathVariable Long userId){
        var getAllDevicesByUsernameQuery = new GetAllDevicesByUserIdQuery(userId);
        var devices = deviceQueryService.handle(getAllDevicesByUsernameQuery);
        var devicesResource = devices.stream().map(DeviceResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(devicesResource);
    }
}
