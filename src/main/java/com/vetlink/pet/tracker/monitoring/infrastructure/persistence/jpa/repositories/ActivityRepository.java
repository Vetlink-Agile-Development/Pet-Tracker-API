package com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Activity;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ActivityType;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByPetTrackerDeviceRecordId(PetTrackerDeviceRecordId petTrackerDeviceRecordId);
    List<Activity> findAllByPetTrackerDeviceRecordIdAndActivityType(PetTrackerDeviceRecordId petTrackerDeviceRecordId, ActivityType activityType);
}
