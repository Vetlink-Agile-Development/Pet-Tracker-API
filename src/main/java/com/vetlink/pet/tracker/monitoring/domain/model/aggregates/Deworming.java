package com.vetlink.pet.tracker.monitoring.domain.model.aggregates;

import com.vetlink.pet.tracker.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/**
 * Deworming aggregate for tracking pet deworming treatments.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Deworming extends AuditableAbstractAggregateRoot<Deworming> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column
    private String dose;

    @Column
    private String batch;

    @Column
    private String veterinarian;

    @Column(nullable = false)
    private LocalDate dateAdministered;

    @Column
    private LocalDate nextDueDate;

    @Column(length = 1000)
    private String observations;

    @Column(nullable = false)
    private String deviceId;

    @Column
    private String documentPath; // Ruta del certificado / documento (opcional)
}
