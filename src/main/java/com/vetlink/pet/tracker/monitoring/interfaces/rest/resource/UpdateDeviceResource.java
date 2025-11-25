package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

public record UpdateDeviceResource(String bearer, String deviceNickname, String deviceCareModes, String deviceStatuses) {
}
