package com.vetlink.pet.tracker.monitoring.application.internal.queyservices;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.GeoFence;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllGeoFencesByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetGeoFenceByIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.services.GeoFenceQueryService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.GeoFenceRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeoFenceQueryServiceImpl implements GeoFenceQueryService {

    private final GeoFenceRepository geoFenceRepository;
    private final DeviceRepository deviceRepository;

    public GeoFenceQueryServiceImpl(GeoFenceRepository geoFenceRepository, DeviceRepository deviceRepository) {
        this.geoFenceRepository = geoFenceRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<GeoFence> handle(GetAllGeoFencesByPetTrackerDeviceRecordIdQuery query) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
        if (device.isEmpty()){
            throw new ResourceNotFoundException("Device not found");
        }
        return geoFenceRepository.findAllByPetTrackerDeviceRecordId(device.get().getPetTrackerDeviceRecordId());
    }

    @Override
    public Optional<GeoFence> handle(GetGeoFenceByIdQuery query) {
        var geoFence = geoFenceRepository.findById(query.geoFenceId());
        if (geoFence.isEmpty()){
            throw new ResourceNotFoundException("GeoFence not found");
        }
        return geoFence;
    }
}
