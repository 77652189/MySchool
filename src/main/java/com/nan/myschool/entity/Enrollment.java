package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Enrollment实体类 - 报名记录
 */
@Entity
@Table(name = "enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pet_id", "course_section_id"}))
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
     * 训练评级（Excellent/Good/Fair/NeedsWork）
     */
    @Column(length = 20)
    private String rating;

    /**
     * 训练进度备注
     */
    @Column(length = 500)
    private String progressNotes;

    /**
     * 完成状态
     */
    @Column(length = 20)
    private String status; // InProgress/Completed/Dropped
}