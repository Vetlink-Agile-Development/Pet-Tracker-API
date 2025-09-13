package com.vetlink.pet.tracker.monitoring.application.internal.commandservices;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.GeoFence;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateGeoFenceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateGeoFenceCommand;
import com.vetlink.pet.tracker.monitoring.domain.services.GeoFenceCommandService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.GeoFenceRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import com.vetlink.pet.tracker.shared.domain.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeoFenceCommandServiceImpl implements GeoFenceCommandService {

    private final GeoFenceRepository geoFenceRepository;
    private final DeviceRepository deviceRepository;

    public GeoFenceCommandServiceImpl(GeoFenceRepository geoFenceRepository, DeviceRepository deviceRepository) {
        this.geoFenceRepository = geoFenceRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<GeoFence> handle(CreateGeoFenceCommand command) {
        var existsByNameAndPetTrackerDeviceRecordId= geoFenceRepository.existsByNameAndPetTrackerDeviceRecordId(command.name(), command.petTrackerDeviceRecordId());
        if (existsByNameAndPetTrackerDeviceRecordId) {
            throw new ValidationException("GeoFence with the same name and device already exists");
        }
        var device = deviceRepository.findByPetTrackerDeviceRecordId(command.petTrackerDeviceRecordId());
        if (device.isEmpty()){
            throw new ResourceNotFoundException("Device not found");
        }
        var geoFence = new GeoFence(command);
        geoFenceRepository.save(geoFence);
        return geoFenceRepository.findByNameAndPetTrackerDeviceRecordId(geoFence.getName(), geoFence.getPetTrackerDeviceRecordId());
    }

    @Override
    public Optional<GeoFence> handle(UpdateGeoFenceCommand command) {
        var geoFence = geoFenceRepository.findById(command.geoFenceId());
        if (geoFence.isEmpty()) {
            throw new ValidationException("GeoFence not found");
        }
        geoFence.get().updateGeofence(command);
        geoFenceRepository.save(geoFence.get());
        return geoFence;
    }
}
