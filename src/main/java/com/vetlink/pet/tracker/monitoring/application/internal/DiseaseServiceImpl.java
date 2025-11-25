package com.vetlink.pet.tracker.monitoring.application.internal;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Disease;
import com.vetlink.pet.tracker.monitoring.domain.services.DiseaseService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de DiseaseService.
 */
@Service
public class DiseaseServiceImpl implements DiseaseService {
    private final DiseaseRepository diseaseRepository;

    @Autowired
    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public List<Disease> getDiseasesByDeviceId(String deviceId) {
        return diseaseRepository.findByDeviceId(deviceId);
    }

    @Override
    public Optional<Disease> getDiseaseById(Long diseaseId, String deviceId) {
        return diseaseRepository.findById(diseaseId)
                .filter(d -> d.getDeviceId().equals(deviceId));
    }

    @Override
    public Disease createDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public Disease updateDisease(Long diseaseId, String deviceId, Disease updatedDisease) {
        Optional<Disease> existing = getDiseaseById(diseaseId, deviceId);
        if (existing.isEmpty()) throw new RuntimeException("Disease not found");
        Disease disease = existing.get();
        disease.setName(updatedDisease.getName());
        disease.setDiagnosisDate(updatedDisease.getDiagnosisDate());
        disease.setSymptoms(updatedDisease.getSymptoms());
        disease.setTreatment(updatedDisease.getTreatment());
        disease.setObservations(updatedDisease.getObservations());
        disease.setImagePath(updatedDisease.getImagePath());
        return diseaseRepository.save(disease);
    }

    @Override
    public void deleteDisease(Long diseaseId, String deviceId) {
        Optional<Disease> existing = getDiseaseById(diseaseId, deviceId);
        existing.ifPresent(diseaseRepository::delete);
    }
}
