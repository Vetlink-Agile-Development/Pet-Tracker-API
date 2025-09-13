package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.DeviceCareModes;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.DeviceStatuses;

public record UpdateDeviceResource(String bearer, String deviceNickname, String deviceCareModes, String deviceStatuses) {
}
