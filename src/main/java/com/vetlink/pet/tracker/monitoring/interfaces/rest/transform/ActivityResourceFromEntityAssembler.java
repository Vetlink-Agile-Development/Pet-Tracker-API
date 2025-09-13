package com.vetlink.pet.tracker.monitoring.interfaces.rest.transform;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Activity;
import com.vetlink.pet.tracker.monitoring.interfaces.rest.resource.ActivityResource;

public class ActivityResourceFromEntityAssembler {
    public static ActivityResource toResourceFromEntity(Activity activity){
        return new ActivityResource(
                activity.getPetTrackerDeviceRecordId().deviceRecordId(),
                activity.getActivityEventName().toString(),
                activity.getActivityType().toString(),
                activity.getCreatedAt()
        );
    }
}
