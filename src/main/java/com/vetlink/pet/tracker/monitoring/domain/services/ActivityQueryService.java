package com.vetlink.pet.tracker.monitoring.domain.services;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Activity;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllActivitiesByPetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllActivitiesByPetTrackerDeviceRecordIdAndActivityType;

import java.util.List;

public interface ActivityQueryService {
    List<Activity> handle(GetAllActivitiesByPetTrackerDeviceRecordId query);
    List<Activity> handle(GetAllActivitiesByPetTrackerDeviceRecordIdAndActivityType query);
}
