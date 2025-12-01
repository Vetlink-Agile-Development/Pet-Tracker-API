package com.vetlink.pet.tracker.monitoring.application.internal;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Vaccination;
import com.vetlink.pet.tracker.monitoring.domain.services.VaccinationService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.VaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VaccinationServiceImpl implements VaccinationService {
    private final VaccinationRepository vaccinationRepository;

    @Autowired
    public VaccinationServiceImpl(VaccinationRepository vaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
    }

    @Override
    public List<Vaccination> getVaccinationsByDeviceId(String deviceId) {
        return vaccinationRepository.findByDeviceId(deviceId);
    }

    @Override
    public Optional<Vaccination> getVaccinationById(Long vaccinationId, String deviceId) {
        return vaccinationRepository.findById(vaccinationId)
                .filter(v -> v.getDeviceId().equals(deviceId));
    }

    @Override
    public Vaccination createVaccination(Vaccination vaccination) {
        return vaccinationRepository.save(vaccination);
    }

    @Override
    public Vaccination updateVaccination(Long vaccinationId, String deviceId, Vaccination updatedVaccination) {
        Optional<Vaccination> existing = getVaccinationById(vaccinationId, deviceId);
        if (existing.isEmpty()) throw new RuntimeException("Vaccination not found");
        Vaccination vacc = existing.get();
        vacc.setVaccineName(updatedVaccination.getVaccineName());
        vacc.setBatch(updatedVaccination.getBatch());
        vacc.setVeterinarian(updatedVaccination.getVeterinarian());
        vacc.setDateAdministered(updatedVaccination.getDateAdministered());
        vacc.setNextDueDate(updatedVaccination.getNextDueDate());
        vacc.setObservations(updatedVaccination.getObservations());
        vacc.setDocumentPath(updatedVaccination.getDocumentPath());
        return vaccinationRepository.save(vacc);
    }

    @Override
    public void deleteVaccination(Long vaccinationId, String deviceId) {
        Optional<Vaccination> existing = getVaccinationById(vaccinationId, deviceId);
        existing.ifPresent(vaccinationRepository::delete);
    }
}