package com.vetlink.pet.tracker.monitoring.domain.model.aggregates;

import com.vetlink.pet.tracker.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/**
 * Disease aggregate for tracking pet diseases.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Disease extends AuditableAbstractAggregateRoot<Disease> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate diagnosisDate;

    @Column(nullable = false, length = 1000)
    private String symptoms;

    @Column(nullable = false, length = 1000)
    private String treatment;

    @Column(length = 1000)
    private String observations;

    @Column(nullable = false)
    private String deviceId;

    @Column
    private String imagePath; // Ruta de la imagen en disco (opcional)
}
