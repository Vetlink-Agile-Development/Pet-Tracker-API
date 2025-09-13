package com.vetlink.pet.tracker.monitoring.domain.model.aggregates;

import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.BeatsPerMinute;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.SaturationOfPeripheralOxygen;
import com.vetlink.pet.tracker.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
public class HealthMeasure extends AuditableAbstractAggregateRoot<HealthMeasure> {

    @Getter
    @Embedded
    @Column(name = "bpm")
    private BeatsPerMinute bpm;

    @Getter
    @Embedded
    @Column(name = "spo2")
    private SaturationOfPeripheralOxygen spo2;

    @Getter
    @Embedded
    @Column(name = "guardian_area_device_id")
    private PetTrackerDeviceRecordId petTrackerDeviceRecordId;

    public HealthMeasure() {}
    public HealthMeasure(BeatsPerMinute bpm, SaturationOfPeripheralOxygen spo2, PetTrackerDeviceRecordId petTrackerDeviceRecordId) {
        this.bpm = bpm;
        this.spo2 = spo2;
        this.petTrackerDeviceRecordId = petTrackerDeviceRecordId;
    }
}
