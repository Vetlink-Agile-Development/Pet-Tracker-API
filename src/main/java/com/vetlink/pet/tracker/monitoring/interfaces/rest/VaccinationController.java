package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Vaccination;
import com.vetlink.pet.tracker.monitoring.domain.services.VaccinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/devices/{deviceId}/vaccinations")
@Tag(name = "Vaccination", description = "Gestión de vacunas de mascotas")
public class VaccinationController {
    private final VaccinationService vaccinationService;

    @Autowired
    public VaccinationController(VaccinationService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }

    @Operation(summary = "Listar vacunas de un dispositivo")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Vaccination>> getVaccinations(@PathVariable String deviceId) {
        return ResponseEntity.ok(vaccinationService.getVaccinationsByDeviceId(deviceId));
    }

    @Operation(summary = "Detalle de vacunación")
    @GetMapping("/{vaccinationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Vaccination> getVaccination(@PathVariable String deviceId, @PathVariable Long vaccinationId) {
        Optional<Vaccination> vacc = vaccinationService.getVaccinationById(vaccinationId, deviceId);
        return vacc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear registro de vacuna")
    @PostMapping(consumes = {"application/json"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Vaccination> createVaccination(
            @PathVariable String deviceId,
            @RequestBody Vaccination payload
    ) {
        // Forzamos deviceId de la ruta, ignorando deviceId que venga en body
        payload.setDeviceId(deviceId);
        // TODO: agregar validaciones básicas si quieres (vaccineName y dateAdministered)
        Vaccination created = vaccinationService.createVaccination(payload);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Editar registro de vacuna")
    @PutMapping(value = "/{vaccinationId}", consumes = {"application/json"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Vaccination> updateVaccination(
            @PathVariable String deviceId,
            @PathVariable Long vaccinationId,
            @RequestBody Vaccination payload
    ) {
        payload.setDeviceId(deviceId);
        Vaccination updated = vaccinationService.updateVaccination(vaccinationId, deviceId, payload);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar registro de vacuna")
    @DeleteMapping("/{vaccinationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteVaccination(@PathVariable String deviceId, @PathVariable Long vaccinationId) {
        vaccinationService.deleteVaccination(vaccinationId, deviceId);
        return ResponseEntity.noContent().build();
    }
}