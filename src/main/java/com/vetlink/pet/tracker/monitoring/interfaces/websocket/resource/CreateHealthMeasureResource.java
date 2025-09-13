package com.vetlink.pet.tracker.monitoring.interfaces.websocket.resource;

public record CreateHealthMeasureResource(int bpm, int spo2, String petTrackerDeviceRecordId) {
}
