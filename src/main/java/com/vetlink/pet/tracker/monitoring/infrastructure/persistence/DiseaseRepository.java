package com.vetlink.pet.tracker.monitoring.infrastructure.persistence;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para Disease.
 */
@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findByDeviceId(String deviceId);
}
