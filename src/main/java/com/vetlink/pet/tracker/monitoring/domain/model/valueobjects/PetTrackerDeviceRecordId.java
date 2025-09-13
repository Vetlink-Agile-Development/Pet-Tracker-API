package com.vetlink.pet.tracker.monitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record PetTrackerDeviceRecordId(String deviceRecordId) {
    public PetTrackerDeviceRecordId() { this(UUID.randomUUID().toString()); }

    public PetTrackerDeviceRecordId {
        if (deviceRecordId == null || deviceRecordId.isBlank()) {
            throw new IllegalArgumentException("Acme device record userId cannot be null or blank");
        }
    }
}
