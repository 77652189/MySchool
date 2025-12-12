package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Course Entity - Training Courses
 */
@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    /**
     * Course title
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Course description
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Number of sessions
     */
    @Column(nullable = false)
    private Integer sessionCount;

    /**
     * Training type (Basic Obedience / Agility Training / Behavior Correction / Social Training)
     */
    @Column(nullable = false, length = 100)
    private String trainingType;

    /**
     * Suitable pet types
     */
    @Column(length = 100)
    private String suitableFor;

    @Override
    public String toString() {
        return title + " (" + sessionCount + " sessions)";
    }
}
