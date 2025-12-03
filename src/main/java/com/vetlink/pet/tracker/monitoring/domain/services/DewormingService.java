package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Deworming;
import java.util.List;
import java.util.Optional;

public interface DewormingService {
    List<Deworming> getDewormingsByDeviceId(String deviceId);
    Optional<Deworming> getDewormingById(Long dewormingId, String deviceId);
    Deworming createDeworming(Deworming deworming);
    Deworming updateDeworming(Long dewormingId, String deviceId, Deworming deworming);
    void deleteDeworming(Long dewormingId, String deviceId);
}
