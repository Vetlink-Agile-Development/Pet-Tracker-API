package com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.HealthMeasure;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.PetTrackerDeviceRecordId;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.HealthMeasureDailyAverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthMeasureRepository extends JpaRepository<HealthMeasure, Long> {
    List<HealthMeasure> findAllByPetTrackerDeviceRecordId(PetTrackerDeviceRecordId petTrackerDeviceRecordId);
    @Query("SELECT " +
            "NEW com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.HealthMeasureDailyAverage( " +
            "    FUNCTION('DATE', h.createdAt), " +
            "    AVG(h.bpm.bpm), " +
            "    AVG(h.spo2.spo2) " +
            ") " +
            "FROM HealthMeasure h " +
            "WHERE FUNCTION('MONTH', h.createdAt) = :month " +
            "AND FUNCTION('YEAR', h.createdAt) = :year " +
            "AND h.petTrackerDeviceRecordId = :petTrackerDeviceRecordId " +
            "GROUP BY FUNCTION('DATE', h.createdAt) " +
            "ORDER BY FUNCTION('DATE', h.createdAt) ASC")
    List<HealthMeasureDailyAverage> findDailyAveragesForCurrentMonthAndGuardian(
            @Param("month") int month,
            @Param("year") int year,
            @Param("petTrackerDeviceRecordId") PetTrackerDeviceRecordId petTrackerDeviceRecordId);

}
