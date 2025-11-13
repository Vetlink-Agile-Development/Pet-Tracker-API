package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllDevicesByUserIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllDevicesQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetDeviceByPetTrackerDeviceRecordIdQuery;

import java.util.List;
import java.util.Optional;

public interface DeviceQueryService {
    List<Device> handle(GetAllDevicesByUserIdQuery query);
    Optional<Device> handle(GetDeviceByPetTrackerDeviceRecordIdQuery query);
    List<Device> handle(GetAllDevicesQuery query);
}
