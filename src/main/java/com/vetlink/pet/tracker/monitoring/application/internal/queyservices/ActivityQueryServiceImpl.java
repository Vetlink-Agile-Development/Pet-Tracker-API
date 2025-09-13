package com.vetlink.pet.tracker.monitoring.application.internal.queyservices;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Activity;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllActivitiesByPetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllActivitiesByPetTrackerDeviceRecordIdAndActivityType;
import com.vetlink.pet.tracker.monitoring.domain.services.ActivityQueryService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.ActivityRepository;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityQueryServiceImpl implements ActivityQueryService {

    private final ActivityRepository activityRepository;
    private final DeviceRepository deviceRepository;

    public ActivityQueryServiceImpl(ActivityRepository activityRepository, DeviceRepository deviceRepository) {
        this.activityRepository = activityRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Activity> handle(GetAllActivitiesByPetTrackerDeviceRecordId query) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ResourceNotFoundException("Device not found");
        }
        return activityRepository.findAllByPetTrackerDeviceRecordId(device.get().getPetTrackerDeviceRecordId());
    }

    @Override
    public List<Activity> handle(GetAllActivitiesByPetTrackerDeviceRecordIdAndActivityType query) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ResourceNotFoundException("Device not found");
        }
        var petTrackerDeviceRecordId = device.get().getPetTrackerDeviceRecordId();
        var activityType = query.activityType();
        return activityRepository.findAllByPetTrackerDeviceRecordIdAndActivityType(petTrackerDeviceRecordId, activityType);
    }
}
