package com.vetlink.pet.tracker.healthmeasure.service.impl;

import com.vetlink.pet.tracker.monitoring.application.internal.commandservices.HealthMeasureCommandServiceImp;
import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.HealthMeasure;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.CreateHealthMeasureCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ApiKey;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.BeatsPerMinute;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.SaturationOfPeripheralOxygen;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.HealthMeasureRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthMeasureCommandServiceImpTest {

    private HealthMeasureCommandServiceImp service;

    @Mock
    private HealthMeasureRepository healthMeasureRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new HealthMeasureCommandServiceImp(healthMeasureRepository, deviceRepository);
    }

    @Test
    void handle_ShouldCreateHealthMeasure_WhenDeviceIsValid() {
        // Arrange
        var bpm = new BeatsPerMinute(80);
        var spo2 = new SaturationOfPeripheralOxygen(98);
        var petTrackerDeviceRecordId = new PetTrackerDeviceRecordId("70fb68f3-aa7e-4108-9417-0bd1fb84fa77");
        var apiKey = new ApiKey("api-key-abc");
        var command = new CreateHealthMeasureCommand(bpm, spo2, petTrackerDeviceRecordId, apiKey);
        var device = mock(Device.class); // Simula un dispositivo válido
        var healthMeasure = new HealthMeasure(bpm, spo2, petTrackerDeviceRecordId);

        when(deviceRepository.findByPetTrackerDeviceRecordIdAndApiKey(petTrackerDeviceRecordId, apiKey))
                .thenReturn(Optional.of(device));
        when(healthMeasureRepository.save(any(HealthMeasure.class))).thenReturn(healthMeasure);

        // Act
        Optional<HealthMeasure> result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(80, result.get().getBpm().bpm());
        assertEquals(98, result.get().getSpo2().spo2());
        assertEquals("70fb68f3-aa7e-4108-9417-0bd1fb84fa77", result.get().getPetTrackerDeviceRecordId().deviceRecordId());

        verify(deviceRepository, times(1)).findByPetTrackerDeviceRecordIdAndApiKey(petTrackerDeviceRecordId, apiKey);
        verify(healthMeasureRepository, times(1)).save(any(HealthMeasure.class));
    }

    @Test
    void handle_ShouldThrowValidationException_WhenDeviceIsInvalid() {
        // Arrange
        var bpm = new BeatsPerMinute(80);
        var spo2 = new SaturationOfPeripheralOxygen(98);
        var petTrackerDeviceRecordId = new PetTrackerDeviceRecordId("device-id-123");
        var apiKey = new ApiKey("api-key-abc");
        var command = new CreateHealthMeasureCommand(bpm, spo2, petTrackerDeviceRecordId, apiKey);

        when(deviceRepository.findByPetTrackerDeviceRecordIdAndApiKey(petTrackerDeviceRecordId, apiKey))
                .thenReturn(Optional.empty());

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> service.handle(command));
        assertEquals("Incorrect device Id or API key", exception.getMessage());

        verify(deviceRepository, times(1)).findByPetTrackerDeviceRecordIdAndApiKey(petTrackerDeviceRecordId, apiKey);
        verify(healthMeasureRepository, never()).save(any(HealthMeasure.class));
    }
}