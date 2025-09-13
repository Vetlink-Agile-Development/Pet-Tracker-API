package com.vetlink.pet.tracker.monitoring.application.internal.queyservices;

import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetHealthThresholdsByPetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.HealthThresholds;
import com.vetlink.pet.tracker.monitoring.domain.services.HealthThresholdQueryService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HealthThresholdQueryServiceImpl implements HealthThresholdQueryService {

    private final DeviceRepository deviceRepository;

    public HealthThresholdQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public HealthThresholds handle(GetHealthThresholdsByPetTrackerDeviceRecordId query) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ResourceNotFoundException("No device found");
        }
        return device.get().getHealthThresholds();
    }
}
