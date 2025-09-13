package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

public record DeviceHealthThresholdsResource(
    String deviceRecordId,
    int minBpm,
    int maxBpm,
    int minSpO2,
    int maxSpO2
) {
}
