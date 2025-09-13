package com.vetlink.pet.tracker.monitoring.domain.model.aggregates;

import com.vetlink.pet.tracker.monitoring.domain.model.commands.AssignDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.RegisterDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateDeviceCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.commands.UpdateHealthThresholdsCommand;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.*;
import com.vetlink.pet.tracker.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Device extends AuditableAbstractAggregateRoot<Device> {

    @Getter
    private String deviceNickname;

    @Getter
    private String bearer;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatuses deviceStatuses;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "care_mode")
    private DeviceCareModes deviceCareModes;

    @Setter
    @Getter
    @Embedded
    private UserId userId;

    @Getter
    @Embedded
    @Column(name = "pet_tracker_device_id")
    private PetTrackerDeviceRecordId petTrackerDeviceRecordId;

    @Getter
    @Embedded
    private ApiKey apiKey;

    @Getter
    @Setter
    @Embedded
    private HealthThresholds healthThresholds;

    public Device() {
        this.healthThresholds = new HealthThresholds();
        this.userId = new UserId();
        this.deviceStatuses = DeviceStatuses.DISCONNECTED;
        this.deviceCareModes = DeviceCareModes.SMALL;
        this.petTrackerDeviceRecordId = new PetTrackerDeviceRecordId();
    }

    public Device(UserId userId) {
        this();
        this.userId = userId;
    }

    public void assignDevice(AssignDeviceCommand command){
        this.userId = new UserId(command.userId());
        this.petTrackerDeviceRecordId = command.petTrackerDeviceRecordId();
        this.deviceNickname = "-";
        this.bearer = "-";
        this.deviceCareModes = DeviceCareModes.SMALL;
        this.deviceStatuses = DeviceStatuses.CONNECTED;
    }

    public Device(RegisterDeviceCommand command, String apiKey) {
        this.healthThresholds = new HealthThresholds();
        this.userId = null;
        this.deviceNickname = null;
        this.bearer = null;
        this.deviceStatuses = DeviceStatuses.DISCONNECTED;
        this.deviceCareModes = DeviceCareModes.SMALL;
        this.petTrackerDeviceRecordId = command.petTrackerDeviceRecordId();
        this.apiKey = new ApiKey(apiKey);
    }

    public void updateDevice(UpdateDeviceCommand command){
        this.deviceNickname = command.deviceNickname();
        this.deviceCareModes = command.deviceCareModes();
        this.bearer = command.bearer();
        this.deviceStatuses = command.deviceStatuses();
    }

    public void UpdateHealthThresholds(UpdateHealthThresholdsCommand command) {
        this.healthThresholds = new HealthThresholds(command.minBpm(), command.maxBpm(),command.minSpO2(), command.maxSpO2());

    }

    public String getDeviceRecordId() {
        return this.petTrackerDeviceRecordId.deviceRecordId();
    }

}
