package com.vetlink.pet.tracker.monitoring.interfaces.rest.resource;

import java.util.Date;

public record ActivityResource(String petTrackerDeviceRecordId, String activityName, String activityType, Date dateAndTime) {
}
