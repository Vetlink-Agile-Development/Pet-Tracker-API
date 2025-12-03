package com.vetlink.pet.tracker.monitoring.infrastructure.persistence;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.Deworming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DewormingRepository extends JpaRepository<Deworming, Long> {
    List<Deworming> findByDeviceId(String deviceId);
}
