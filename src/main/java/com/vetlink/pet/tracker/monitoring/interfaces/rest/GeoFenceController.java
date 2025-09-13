package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateGeoFenceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetGeoFenceByIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.services.GeoFenceCommandService;
import com.vetlink.pet.tracker.monitoring.domain.services.GeoFenceQueryService;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.CreateGeoFenceResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.GeoFenceResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.UpdateGeoFenceResource;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.CreateGeoFenceCommandFromResourceAssembler;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.GeoFenceResourceFromEntityAssembler;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.transform.UpdateGeoFenceCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/geo-fences", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "GeoFences", description = "GeoFences Management Endpoints")
public class GeoFenceController {
    private final GeoFenceCommandService geoFenceCommandService;
    private final GeoFenceQueryService geoFenceQueryService;

    public GeoFenceController(GeoFenceCommandService geoFenceCommandService, GeoFenceQueryService geoFenceQueryService) {
        this.geoFenceCommandService = geoFenceCommandService;
        this.geoFenceQueryService = geoFenceQueryService;
    }

    @PostMapping
    public ResponseEntity<GeoFenceResource> createGeoFence(@RequestBody CreateGeoFenceResource createGeoFenceResource){
        var createGeoFenceCommand = CreateGeoFenceCommandFromResourceAssembler.toCommandFromResource(createGeoFenceResource);
        var geoFence = geoFenceCommandService.handle(createGeoFenceCommand);
        if (geoFence.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var geoFenceResource = GeoFenceResourceFromEntityAssembler.toResourceFromEntity(geoFence.get());
        return new ResponseEntity<>(geoFenceResource, HttpStatus.CREATED);
    }

    @GetMapping("/{geoFenceId}")
    public ResponseEntity<GeoFenceResource> getGeoFenceById(@PathVariable Long geoFenceId) {
        var getGeoFenceByIdQuery = new GetGeoFenceByIdQuery(geoFenceId);
        var geoFence = geoFenceQueryService.handle(getGeoFenceByIdQuery);
        if (geoFence.isEmpty()) return ResponseEntity.notFound().build();
        var geoFenceResource = GeoFenceResourceFromEntityAssembler.toResourceFromEntity(geoFence.get());
        return new ResponseEntity<>(geoFenceResource, HttpStatus.OK);
    }

    @PutMapping("/{geoFenceId}")
    public ResponseEntity<GeoFenceResource> updateGeoFenceById(@PathVariable Long geoFenceId, @RequestBody UpdateGeoFenceResource updateGeoFenceResource) {
        var updateGeoFenceCommand = UpdateGeoFenceCommandFromResourceAssembler.toCommandFromResource(geoFenceId, updateGeoFenceResource);
        var geoFence = geoFenceCommandService.handle(updateGeoFenceCommand);
        if (geoFence.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var geoFenceResource = GeoFenceResourceFromEntityAssembler.toResourceFromEntity(geoFence.get());
        return ResponseEntity.ok(geoFenceResource);
    }
}
