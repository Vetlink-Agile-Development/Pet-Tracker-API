package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.AssignDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.DeleteDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.RegisterDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UnassignDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateHealthThresholdsCommand;

import java.util.Optional;

public interface DeviceCommandService {
    Optional<Device> handle(AssignDeviceCommand command);
    Optional<String> handle(RegisterDeviceCommand command);
    Optional<Device> handle(UpdateDeviceCommand command);
    Optional<Device> handle(UpdateHealthThresholdsCommand command);
    boolean handle(DeleteDeviceCommand command);
    Optional<Device> handle(UnassignDeviceCommand command);
}
