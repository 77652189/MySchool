package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Enrollment Entity - Training Enrollment Record
 */
@Entity
@Table(
        name = "enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pet_id", "course_section_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer enrollmentId;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "course_section_id", nullable = false)
    private CourseSection courseSection;

    /**
     * Training rating (Excellent / Good / Fair / NeedsWork)
     */
    @Column(length = 20)
    private String rating;

    /**
     * Training progress notes
     */
    @Column(length = 500)
    private String progressNotes;

    /**
     * Completion status
     */
    @Column(length = 20)
    private String status; // InProgress / Completed / Dropped
}
