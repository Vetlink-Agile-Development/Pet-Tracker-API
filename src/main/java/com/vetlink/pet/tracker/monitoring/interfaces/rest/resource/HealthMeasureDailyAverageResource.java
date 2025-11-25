package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

public record HealthMeasureDailyAverageResource(Object date, Double avgBpm, Double avgSpo2) {
}
