package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Vaccination;
import com.vetlink.pet.tracker.monitoring.domain.services.VaccinationService;
import com.vetlink.pet.tracker.monitoring.infrastructure.storage.AzureStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/devices/{deviceId}/vaccinations")
@Tag(name = "Vaccination", description = "Gestión de vacunas de mascotas")
public class VaccinationController {
    private final VaccinationService vaccinationService;
    private final AzureStorageService azureStorageService;

    @Autowired
    public VaccinationController(VaccinationService vaccinationService, AzureStorageService azureStorageService) {
        this.vaccinationService = vaccinationService;
        this.azureStorageService = azureStorageService;
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
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Vaccination> createVaccination(
            @PathVariable String deviceId,
            @RequestParam String vaccineName,
            @RequestParam String dateAdministered,
            @RequestParam(required = false) String batch,
            @RequestParam(required = false) String veterinarian,
            @RequestParam(required = false) String nextDueDate,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        Vaccination vaccination = new Vaccination();
        vaccination.setDeviceId(deviceId);
        vaccination.setVaccineName(vaccineName);
        vaccination.setDateAdministered(LocalDate.parse(dateAdministered));
        vaccination.setBatch(batch);
        vaccination.setVeterinarian(veterinarian);
        if (nextDueDate != null && !nextDueDate.isEmpty()) {
            vaccination.setNextDueDate(LocalDate.parse(nextDueDate));
        }
        vaccination.setObservations(observations);
        
        // Upload image to Azure Storage if provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = azureStorageService.uploadFile(image, "vaccinations");
            vaccination.setDocumentPath(imageUrl);
        }
        
        Vaccination created = vaccinationService.createVaccination(vaccination);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Editar registro de vacuna")
    @PutMapping(value = "/{vaccinationId}", consumes = {"multipart/form-data"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Vaccination> updateVaccination(
            @PathVariable String deviceId,
            @PathVariable Long vaccinationId,
            @RequestParam String vaccineName,
            @RequestParam String dateAdministered,
            @RequestParam(required = false) String batch,
            @RequestParam(required = false) String veterinarian,
            @RequestParam(required = false) String nextDueDate,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        // Get existing vaccination to delete old image if new one is uploaded
        Optional<Vaccination> existingVaccination = vaccinationService.getVaccinationById(vaccinationId, deviceId);
        
        Vaccination vaccination = new Vaccination();
        vaccination.setDeviceId(deviceId);
        vaccination.setVaccineName(vaccineName);
        vaccination.setDateAdministered(LocalDate.parse(dateAdministered));
        vaccination.setBatch(batch);
        vaccination.setVeterinarian(veterinarian);
        if (nextDueDate != null && !nextDueDate.isEmpty()) {
            vaccination.setNextDueDate(LocalDate.parse(nextDueDate));
        }
        vaccination.setObservations(observations);
        
        // Upload new image to Azure Storage if provided
        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (existingVaccination.isPresent() && existingVaccination.get().getDocumentPath() != null) {
                azureStorageService.deleteFile(existingVaccination.get().getDocumentPath());
            }
            String imageUrl = azureStorageService.uploadFile(image, "vaccinations");
            vaccination.setDocumentPath(imageUrl);
        } else if (existingVaccination.isPresent()) {
            // Keep existing image if no new image provided
            vaccination.setDocumentPath(existingVaccination.get().getDocumentPath());
        }
        
        Vaccination updated = vaccinationService.updateVaccination(vaccinationId, deviceId, vaccination);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar registro de vacuna")
    @DeleteMapping("/{vaccinationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteVaccination(@PathVariable String deviceId, @PathVariable Long vaccinationId) {
        // Get vaccination to delete associated image from Azure Storage
        Optional<Vaccination> vaccination = vaccinationService.getVaccinationById(vaccinationId, deviceId);
        if (vaccination.isPresent() && vaccination.get().getDocumentPath() != null) {
            azureStorageService.deleteFile(vaccination.get().getDocumentPath());
        }
        
        vaccinationService.deleteVaccination(vaccinationId, deviceId);
        return ResponseEntity.noContent().build();
    }
}