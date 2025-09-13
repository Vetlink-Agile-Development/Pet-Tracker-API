package com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.GeoFence;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeoFenceRepository extends JpaRepository<GeoFence, Long> {
    boolean existsByNameAndPetTrackerDeviceRecordId(String name, PetTrackerDeviceRecordId petTrackerDeviceRecordId);
    Optional<GeoFence> findByPetTrackerDeviceRecordId(PetTrackerDeviceRecordId petTrackerDeviceRecordId);
    Optional<GeoFence> findByNameAndPetTrackerDeviceRecordId(String name, PetTrackerDeviceRecordId petTrackerDeviceRecordId);
    List<GeoFence> findAllByPetTrackerDeviceRecordId(PetTrackerDeviceRecordId petTrackerDeviceRecordId);
}
