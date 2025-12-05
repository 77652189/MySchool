package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * CourseSection实体类 - 训练班级
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
     * 训练场地
     */
    @Column(length = 50)
    private String trainingGround;

    /**
     * 训练时间
     */
    @Column(length = 100)
    private String schedule;

    /**
     * 最大容量
     */
    @Column(nullable = false)
    private Integer capacity;

    @Override
    public String toString() {
        return "第" + sectionId + "班 (场地:" + trainingGround + ")";
    }
}