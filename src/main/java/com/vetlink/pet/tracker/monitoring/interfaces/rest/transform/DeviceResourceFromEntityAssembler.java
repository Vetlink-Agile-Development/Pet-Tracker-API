package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.DeviceResource;

public class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device device){
        return new DeviceResource(
                device.getDeviceRecordId(),
                device.getDeviceNickname(),
                device.getBearer(),
                device.getDeviceCareModes().toString(),
                device.getDeviceStatuses().toString(),
                device.getUserId().userId(),
                device.getApiKey().apiKey()
        );
    }
}
