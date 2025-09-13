package com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Device;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.ApiKey;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAllByUserId(UserId userId);
    Optional<Device> findByPetTrackerDeviceRecordId(PetTrackerDeviceRecordId petTrackerDeviceRecordId);
    boolean existsByPetTrackerDeviceRecordId(PetTrackerDeviceRecordId petTrackerDeviceRecordId);
    Optional<Device> findByPetTrackerDeviceRecordIdAndApiKey(PetTrackerDeviceRecordId petTrackerDeviceRecordId, ApiKey apiKey);
}
