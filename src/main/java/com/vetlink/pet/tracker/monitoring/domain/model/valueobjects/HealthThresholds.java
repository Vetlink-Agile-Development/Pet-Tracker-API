package com.vetlink.pet.tracker.monitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record HealthThresholds(
        int minBpm,
        int maxBpm,
        int minSpO2,
        int maxSpO2
) {
    public HealthThresholds() {
        this(60, 100, 90, 100);
    }
}