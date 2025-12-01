package com.vetlink.pet.tracker.monitoring.infrastructure.persistence;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    List<Vaccination> findByDeviceId(String deviceId);
}