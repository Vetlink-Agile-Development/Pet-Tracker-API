package com.vetlink.pet.tracker.monitoring.domain.model.aggregates;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityEventName;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityType;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
public class Activity extends AuditableAbstractAggregateRoot<Activity> {
    @Getter
    private PetTrackerDeviceRecordId petTrackerDeviceRecordId;

    @Getter
    @Enumerated(EnumType.STRING)
    private ActivityEventName activityEventName;

    @Getter
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    public Activity() {}

    public Activity(PetTrackerDeviceRecordId petTrackerDeviceRecordId, ActivityEventName activityEventName, ActivityType activityType) {
        this.petTrackerDeviceRecordId = petTrackerDeviceRecordId;
        this.activityEventName = activityEventName;
        this.activityType = activityType;
    }
}
