package com.vetlink.pet.tracker.monitoring.application.internal;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Deworming;
import com.vetlink.pet.tracker.monitoring.domain.services.DewormingService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.DewormingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DewormingServiceImpl implements DewormingService {
    private final DewormingRepository dewormingRepository;

    @Autowired
    public DewormingServiceImpl(DewormingRepository dewormingRepository) {
        this.dewormingRepository = dewormingRepository;
    }

    @Override
    public List<Deworming> getDewormingsByDeviceId(String deviceId) {
        return dewormingRepository.findByDeviceId(deviceId);
    }

    @Override
    public Optional<Deworming> getDewormingById(Long dewormingId, String deviceId) {
        return dewormingRepository.findById(dewormingId)
                .filter(d -> d.getDeviceId().equals(deviceId));
    }

    @Override
    public Deworming createDeworming(Deworming deworming) {
        return dewormingRepository.save(deworming);
    }

    @Override
    public Deworming updateDeworming(Long dewormingId, String deviceId, Deworming updatedDeworming) {
        Optional<Deworming> existing = getDewormingById(dewormingId, deviceId);
        if (existing.isEmpty()) throw new RuntimeException("Deworming not found");
        Deworming deworming = existing.get();
        deworming.setProductName(updatedDeworming.getProductName());
        deworming.setDose(updatedDeworming.getDose());
        deworming.setBatch(updatedDeworming.getBatch());
        deworming.setVeterinarian(updatedDeworming.getVeterinarian());
        deworming.setDateAdministered(updatedDeworming.getDateAdministered());
        deworming.setNextDueDate(updatedDeworming.getNextDueDate());
        deworming.setObservations(updatedDeworming.getObservations());
        deworming.setDocumentPath(updatedDeworming.getDocumentPath());
        return dewormingRepository.save(deworming);
    }

    @Override
    public void deleteDeworming(Long dewormingId, String deviceId) {
        Optional<Deworming> existing = getDewormingById(dewormingId, deviceId);
        existing.ifPresent(dewormingRepository::delete);
    }
}
