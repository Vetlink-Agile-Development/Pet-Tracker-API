package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

public record DeviceResource(String petTrackerDeviceRecordId, String nickname, String bearer, String careMode, String status, Long userId, String apiKey) {
}
