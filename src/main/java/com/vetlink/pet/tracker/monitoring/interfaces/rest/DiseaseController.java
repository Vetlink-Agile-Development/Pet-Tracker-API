package com.vetlink.pet.tracker.monitoring.interfaces.rest;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Disease;
import com.vetlink.pet.tracker.monitoring.domain.services.DiseaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/devices/{deviceId}/diseases")
@Tag(name = "Disease", description = "Gestión de enfermedades de mascotas")
public class DiseaseController {
    private final DiseaseService diseaseService;
    private final String IMAGE_DIR = "uploads/diseases";

    @Autowired
    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
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
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            disease.setImagePath(imagePath);
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
        Disease disease = new Disease();
        disease.setDeviceId(deviceId);
        disease.setName(name);
        disease.setDiagnosisDate(java.time.LocalDate.parse(diagnosisDate));
        disease.setSymptoms(symptoms);
        disease.setTreatment(treatment);
        disease.setObservations(observations);
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            disease.setImagePath(imagePath);
        }
        Disease updated = diseaseService.updateDisease(diseaseId, deviceId, disease);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar enfermedad")
    @DeleteMapping("/{diseaseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDisease(@PathVariable String deviceId, @PathVariable Long diseaseId) {
        diseaseService.deleteDisease(diseaseId, deviceId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Guarda la imagen en disco y retorna la ruta relativa.
     */
    private String saveImage(MultipartFile image) throws IOException {
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) dir.mkdirs();
        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path path = Paths.get(IMAGE_DIR, filename);
        Files.write(path, image.getBytes());
        return path.toString();
    }
}
