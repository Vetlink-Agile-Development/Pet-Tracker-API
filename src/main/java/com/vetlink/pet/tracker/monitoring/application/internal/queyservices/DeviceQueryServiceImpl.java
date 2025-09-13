package com.vetlink.pet.tracker.monitoring.application.internal.queyservices;

import com.vetlink.pet.tracker.monitoring.application.internal.outboundservices.acl.ExternalIamService;
import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllDevicesByUserIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetDeviceByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceQueryService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;
    private final ExternalIamService externalIamService;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository, ExternalIamService externalIamService) {
        this.deviceRepository = deviceRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public List<Device> handle(GetAllDevicesByUserIdQuery query) {
        var userId = externalIamService.fetchUsernameById(query.userId());
        if (userId.isEmpty()) {
            throw new ResourceNotFoundException("User id not found");
        }
        return deviceRepository.findAllByUserId(userId.get());
    }

    @Override
    public Optional<Device> handle(GetDeviceByPetTrackerDeviceRecordIdQuery query) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
        if (device.isEmpty()){
            throw new ResourceNotFoundException("Device not found");
        }
        return device;
    }
}
