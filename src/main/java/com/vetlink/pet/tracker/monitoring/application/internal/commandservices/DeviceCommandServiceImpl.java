package com.vetlink.pet.tracker.monitoring.application.internal.commandservices;

import com.vetlink.pet.tracker.monitoring.application.internal.outboundservices.SecureApiKeyGenerator;
import com.vetlink.pet.tracker.monitoring.application.internal.outboundservices.acl.ExternalIamService;
import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.AssignDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.RegisterDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateHealthThresholdsCommand;
import com.vetlink.pet.tracker.monitoring.domain.services.DeviceCommandService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import com.vetlink.pet.tracker.shared.domain.exceptions.ValidationException;
import com.vetlink.pet.tracker.shared.domain.model.valueobjects.GuidValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;
    private final ExternalIamService externalIamService;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository, ExternalIamService externalIamService) {
        this.deviceRepository = deviceRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public Optional<Device> handle(AssignDeviceCommand command) {
        var userId = externalIamService.fetchUsernameById(command.userId());
        if (userId.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        var device = deviceRepository.findByPetTrackerDeviceRecordId(command.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ResourceNotFoundException("Device not found");
        }
        if (device.get().getUserId() != null){
            throw new ValidationException("Device is already assigned");
        }
        device.get().assignDevice(command);
        deviceRepository.save(device.get());
        return deviceRepository.findByPetTrackerDeviceRecordId(device.get().getPetTrackerDeviceRecordId());
    }

    @Override
    public Optional<String> handle(RegisterDeviceCommand command) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(command.petTrackerDeviceRecordId());
        if (!GuidValidator.isValidGuid(command.petTrackerDeviceRecordId().deviceRecordId())){
            throw new ValidationException("PetTrackerDeviceRecordId must be guid");
        }
        if (device.isPresent()) {
            throw new ValidationException("Device is already registered");
        }
        var apiKey =  SecureApiKeyGenerator.generateToken();
        var newDevice = new Device(command, apiKey);
        deviceRepository.save(newDevice);
        return Optional.of(apiKey);
    }

    @Override
    public Optional<Device> handle(UpdateDeviceCommand command) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(command.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ValidationException("Device not found");
        }
        device.get().updateDevice(command);
        deviceRepository.save(device.get());
        return device;
    }

    @Override
    public Optional<Device> handle(UpdateHealthThresholdsCommand command) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(command.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ValidationException("Device not found");
        }
        device.get().UpdateHealthThresholds(command);
        deviceRepository.save(device.get());
        return device;
    }
}
