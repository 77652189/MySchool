package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * CourseSection Entity - Training Class Section
 */
@Entity
@Table(name = "course_section")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sectionId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    /**
     * Training ground
     */
    @Column(length = 50)
    private String trainingGround;

    /**
     * Training schedule
     */
    @Column(length = 100)
    private String schedule;

    /**
     * Maximum capacity
     */
    @Column(nullable = false)
    private Integer capacity;

    @Override
    public String toString() {
        return "Section " + sectionId + " (Ground: " + trainingGround + ")";
    }
}
