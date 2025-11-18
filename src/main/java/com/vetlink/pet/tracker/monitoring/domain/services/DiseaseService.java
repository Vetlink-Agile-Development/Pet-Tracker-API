package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Disease;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio para Disease.
 */
public interface DiseaseService {
    List<Disease> getDiseasesByDeviceId(String deviceId);
    Optional<Disease> getDiseaseById(Long diseaseId, String deviceId);
    Disease createDisease(Disease disease);
    Disease updateDisease(Long diseaseId, String deviceId, Disease disease);
    void deleteDisease(Long diseaseId, String deviceId);
}
