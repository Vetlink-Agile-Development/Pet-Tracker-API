package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Deworming;
import com.vetlink.pet.tracker.monitoring.domain.services.DewormingService;
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
@RequestMapping("/api/v1/devices/{deviceId}/dewormings")
@Tag(name = "Deworming", description = "Gestión de desparasitaciones de mascotas")
public class DewormingController {
    private final DewormingService dewormingService;
    private final AzureStorageService azureStorageService;

    @Autowired
    public DewormingController(DewormingService dewormingService, AzureStorageService azureStorageService) {
        this.dewormingService = dewormingService;
        this.azureStorageService = azureStorageService;
    }

    @Operation(summary = "Listar desparasitaciones de un dispositivo")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Deworming>> getDewormings(@PathVariable String deviceId) {
        return ResponseEntity.ok(dewormingService.getDewormingsByDeviceId(deviceId));
    }

    @Operation(summary = "Detalle de desparasitación")
    @GetMapping("/{dewormingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Deworming> getDeworming(@PathVariable String deviceId, @PathVariable Long dewormingId) {
        Optional<Deworming> deworming = dewormingService.getDewormingById(dewormingId, deviceId);
        return deworming.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear registro de desparasitación")
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Deworming> createDeworming(
            @PathVariable String deviceId,
            @RequestParam String productName,
            @RequestParam String dateAdministered,
            @RequestParam(required = false) String dose,
            @RequestParam(required = false) String batch,
            @RequestParam(required = false) String veterinarian,
            @RequestParam(required = false) String nextDueDate,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        Deworming deworming = new Deworming();
        deworming.setDeviceId(deviceId);
        deworming.setProductName(productName);
        deworming.setDateAdministered(LocalDate.parse(dateAdministered));
        deworming.setDose(dose);
        deworming.setBatch(batch);
        deworming.setVeterinarian(veterinarian);
        if (nextDueDate != null && !nextDueDate.isEmpty()) {
            deworming.setNextDueDate(LocalDate.parse(nextDueDate));
        }
        deworming.setObservations(observations);
        
        // Upload image to Azure Storage if provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = azureStorageService.uploadFile(image, "dewormings");
            deworming.setDocumentPath(imageUrl);
        }
        
        Deworming created = dewormingService.createDeworming(deworming);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Editar registro de desparasitación")
    @PutMapping(value = "/{dewormingId}", consumes = {"multipart/form-data"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Deworming> updateDeworming(
            @PathVariable String deviceId,
            @PathVariable Long dewormingId,
            @RequestParam String productName,
            @RequestParam String dateAdministered,
            @RequestParam(required = false) String dose,
            @RequestParam(required = false) String batch,
            @RequestParam(required = false) String veterinarian,
            @RequestParam(required = false) String nextDueDate,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        // Get existing deworming to delete old image if new one is uploaded
        Optional<Deworming> existingDeworming = dewormingService.getDewormingById(dewormingId, deviceId);
        
        Deworming deworming = new Deworming();
        deworming.setDeviceId(deviceId);
        deworming.setProductName(productName);
        deworming.setDateAdministered(LocalDate.parse(dateAdministered));
        deworming.setDose(dose);
        deworming.setBatch(batch);
        deworming.setVeterinarian(veterinarian);
        if (nextDueDate != null && !nextDueDate.isEmpty()) {
            deworming.setNextDueDate(LocalDate.parse(nextDueDate));
        }
        deworming.setObservations(observations);
        
        // Upload new image to Azure Storage if provided
        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (existingDeworming.isPresent() && existingDeworming.get().getDocumentPath() != null) {
                azureStorageService.deleteFile(existingDeworming.get().getDocumentPath());
            }
            String imageUrl = azureStorageService.uploadFile(image, "dewormings");
            deworming.setDocumentPath(imageUrl);
        } else if (existingDeworming.isPresent()) {
            // Keep existing image if no new image provided
            deworming.setDocumentPath(existingDeworming.get().getDocumentPath());
        }
        
        Deworming updated = dewormingService.updateDeworming(dewormingId, deviceId, deworming);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar registro de desparasitación")
    @DeleteMapping("/{dewormingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDeworming(@PathVariable String deviceId, @PathVariable Long dewormingId) {
        // Get deworming to delete associated image from Azure Storage
        Optional<Deworming> deworming = dewormingService.getDewormingById(dewormingId, deviceId);
        if (deworming.isPresent() && deworming.get().getDocumentPath() != null) {
            azureStorageService.deleteFile(deworming.get().getDocumentPath());
        }
        
        dewormingService.deleteDeworming(dewormingId, deviceId);
        return ResponseEntity.noContent().build();
    }
}
