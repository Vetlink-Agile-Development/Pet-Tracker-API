package com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource;

public record CurrentLocationResource(float latitude, float longitude, String petTrackerDeviceRecordId) {
}
