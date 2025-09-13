package com.vetlink.pet.tracker.monitoring.application.internal.commandservices;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Activity;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateActivityCommand;
import com.vetlink.pet.tracker.monitoring.domain.services.ActivityCommandService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.ActivityRepository;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityCommandServiceImpl implements ActivityCommandService {

    private final ActivityRepository activityRepository;
    private final DeviceRepository deviceRepository;

    public ActivityCommandServiceImpl(ActivityRepository activityRepository, DeviceRepository deviceRepository) {
        this.activityRepository = activityRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Activity> handle(CreateActivityCommand command) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(command.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ResourceNotFoundException("Device not found");
        }
        var activity = new Activity(
                command.petTrackerDeviceRecordId(),
                command.activityEventName(),
                command.activityType()
        );
        var activityCreated = activityRepository.save(activity);
        return Optional.of(activityCreated);
    }
}
