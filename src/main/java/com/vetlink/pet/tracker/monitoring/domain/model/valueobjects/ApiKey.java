package com.vetlink.pet.tracker.monitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ApiKey(String apiKey) {
}
