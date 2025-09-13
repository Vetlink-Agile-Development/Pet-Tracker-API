package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.AssignDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.RegisterDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateHealthThresholdsCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ApiKey;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface DeviceCommandService {
    Optional<Device> handle(AssignDeviceCommand command);
    Optional<String> handle(RegisterDeviceCommand command);
    Optional<Device> handle(UpdateDeviceCommand command);
    Optional<Device> handle(UpdateHealthThresholdsCommand command);
}
