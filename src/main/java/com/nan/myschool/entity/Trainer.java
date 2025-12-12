package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Trainer Entity - Trainer Information
 */
@Entity
@Table(name = "trainer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    private Integer trainerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "trainer_id")
    private User user;

    /**
     * Trainer name
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Specialization (Obedience Training / Agility Training / Behavior Correction / etc.)
     */
    @Column(nullable = false, length = 100)
    private String specialization;

    /**
     * Certification level
     */
    @Column(length = 50)
    private String certificationLevel;

    @Override
    public String toString() {
        return name + " - " + specialization;
    }
}
