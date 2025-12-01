package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Vaccination;
import java.util.List;
import java.util.Optional;

public interface VaccinationService {
    List<Vaccination> getVaccinationsByDeviceId(String deviceId);
    Optional<Vaccination> getVaccinationById(Long vaccinationId, String deviceId);
    Vaccination createVaccination(Vaccination vaccination);
    Vaccination updateVaccination(Long vaccinationId, String deviceId, Vaccination vaccination);
    void deleteVaccination(Long vaccinationId, String deviceId);
}