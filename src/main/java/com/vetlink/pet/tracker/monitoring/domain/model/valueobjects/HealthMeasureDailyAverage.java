package com.vetlink.pet.tracker.monitoring.domain.model.valueobjects;

import lombok.Getter;


@Getter
public class HealthMeasureDailyAverage {

    private Object date;
    private Double avgBpm;
    private Double avgSpo2;

    public HealthMeasureDailyAverage(){

    }
    public HealthMeasureDailyAverage(Object date, Double avgBpm, Double avgSpo2) {
        this.date = date;
        this.avgBpm = avgBpm;
        this.avgSpo2 = avgSpo2;
    }

}