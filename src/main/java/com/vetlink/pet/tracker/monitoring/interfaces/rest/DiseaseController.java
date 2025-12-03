package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Disease;
import com.vetlink.pet.tracker.monitoring.domain.services.DiseaseService;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/devices/{deviceId}/diseases")
@Tag(name = "Disease", description = "Gestión de enfermedades de mascotas")
public class DiseaseController {
    private final DiseaseService diseaseService;
    private final AzureStorageService azureStorageService;

    @Autowired
    public DiseaseController(DiseaseService diseaseService, AzureStorageService azureStorageService) {
        this.diseaseService = diseaseService;
        this.azureStorageService = azureStorageService;
    }

    @Operation(summary = "Listar enfermedades de un dispositivo")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Disease>> getDiseases(@PathVariable String deviceId) {
        return ResponseEntity.ok(diseaseService.getDiseasesByDeviceId(deviceId));
    }

    @Operation(summary = "Detalle de enfermedad")
    @GetMapping("/{diseaseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Disease> getDisease(@PathVariable String deviceId, @PathVariable Long diseaseId) {
        Optional<Disease> disease = diseaseService.getDiseaseById(diseaseId, deviceId);
        return disease.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear enfermedad")
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Disease> createDisease(
            @PathVariable String deviceId,
            @RequestParam String name,
            @RequestParam String diagnosisDate,
            @RequestParam String symptoms,
            @RequestParam String treatment,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        Disease disease = new Disease();
        disease.setDeviceId(deviceId);
        disease.setName(name);
        disease.setDiagnosisDate(java.time.LocalDate.parse(diagnosisDate));
        disease.setSymptoms(symptoms);
        disease.setTreatment(treatment);
        disease.setObservations(observations);
        
        // Upload image to Azure Storage if provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = azureStorageService.uploadFile(image, "diseases");
            disease.setImagePath(imageUrl);
        }
        
        Disease created = diseaseService.createDisease(disease);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Editar enfermedad")
    @PutMapping(value = "/{diseaseId}", consumes = {"multipart/form-data"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Disease> updateDisease(
            @PathVariable String deviceId,
            @PathVariable Long diseaseId,
            @RequestParam String name,
            @RequestParam String diagnosisDate,
            @RequestParam String symptoms,
            @RequestParam String treatment,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        // Get existing disease to delete old image if new one is uploaded
        Optional<Disease> existingDisease = diseaseService.getDiseaseById(diseaseId, deviceId);
        
        Disease disease = new Disease();
        disease.setDeviceId(deviceId);
        disease.setName(name);
        disease.setDiagnosisDate(java.time.LocalDate.parse(diagnosisDate));
        disease.setSymptoms(symptoms);
        disease.setTreatment(treatment);
        disease.setObservations(observations);
        
        // Upload new image to Azure Storage if provided
        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (existingDisease.isPresent() && existingDisease.get().getImagePath() != null) {
                azureStorageService.deleteFile(existingDisease.get().getImagePath());
            }
            String imageUrl = azureStorageService.uploadFile(image, "diseases");
            disease.setImagePath(imageUrl);
        } else if (existingDisease.isPresent()) {
            // Keep existing image if no new image provided
            disease.setImagePath(existingDisease.get().getImagePath());
        }
        
        Disease updated = diseaseService.updateDisease(diseaseId, deviceId, disease);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar enfermedad")
    @DeleteMapping("/{diseaseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDisease(@PathVariable String deviceId, @PathVariable Long diseaseId) {
        // Get disease to delete associated image from Azure Storage
        Optional<Disease> disease = diseaseService.getDiseaseById(diseaseId, deviceId);
        if (disease.isPresent() && disease.get().getImagePath() != null) {
            azureStorageService.deleteFile(disease.get().getImagePath());
        }
        
        diseaseService.deleteDisease(diseaseId, deviceId);
        return ResponseEntity.noContent().build();
    }
}
