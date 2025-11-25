package com.vetlink.pet.tracker.monitoring.application.internal.queyservices;

import com.vetlink.pet.tracker.monitoring.domain.model.aggregates.HealthMeasure;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetAllHealthMeasuresByDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.queries.GetHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.HealthMeasureDailyAverage;
import com.vetlink.pet.tracker.monitoring.domain.services.HealthMeasureQueryService;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.vetlink.pet.tracker.monitoring.infrastructure.persistence.jpa.repositories.HealthMeasureRepository;
import com.vetlink.pet.tracker.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HealthMeasureQueryServiceImp implements HealthMeasureQueryService {

    private final HealthMeasureRepository healthMeasureRepository;
    private final DeviceRepository deviceRepository;

    public HealthMeasureQueryServiceImp(HealthMeasureRepository healthMeasureRepository, DeviceRepository deviceRepository) {
        this.healthMeasureRepository = healthMeasureRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<HealthMeasure> handle(GetAllHealthMeasuresByDeviceRecordIdQuery query) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ResourceNotFoundException("Device does not exist");
        }
        return healthMeasureRepository.findAllByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
    }

    @Override
    public List<HealthMeasureDailyAverage> handle(GetHealthMeasuresDailyAverageFromCurrentMonthByPetTrackerDeviceRecordIdQuery query) {
        var device = deviceRepository.findByPetTrackerDeviceRecordId(query.petTrackerDeviceRecordId());
        if (device.isEmpty()) {
            throw new ResourceNotFoundException("Device does not exist");
        }
        LocalDate currentDate = LocalDate.now();
        int month = query.month() != null ? query.month() : currentDate.getMonthValue();
        int year = query.year() != null ? query.year() : currentDate.getYear();
        return healthMeasureRepository.findDailyAveragesForCurrentMonthAndGuardian(month, year, device.get().getPetTrackerDeviceRecordId());
    }
}
