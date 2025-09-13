package com.vetlink.pet.tracker.monitoring.application.internal.commandservices;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.HealthMeasure;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateHealthMeasureCommand;
import com.vetlink.pet.tracker.monitoring.domain.services.HealthMeasureCommandService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.HealthMeasureRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HealthMeasureCommandServiceImp implements HealthMeasureCommandService {

    private final HealthMeasureRepository healthMeasureRepository;
    private final DeviceRepository deviceRepository;

    public HealthMeasureCommandServiceImp(HealthMeasureRepository healthMeasureRepository, DeviceRepository deviceRepository) {
        this.healthMeasureRepository = healthMeasureRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<HealthMeasure> handle(CreateHealthMeasureCommand command) {
        var device = deviceRepository.findByPetTrackerDeviceRecordIdAndApiKey(command.petTrackerDeviceRecordId(), command.apiKey());
        if (device.isEmpty()) {
            throw new ValidationException("Incorrect device Id or API key");
        }
        var healthMeasure = new HealthMeasure(
                command.bpm(),
                command.spo2(),
                command.petTrackerDeviceRecordId()
        );
        var healthMeasureCreated = healthMeasureRepository.save(healthMeasure);
        return Optional.of(healthMeasureCreated);
    }
}
