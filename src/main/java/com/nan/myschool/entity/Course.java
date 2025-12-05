package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Course实体类 - 训练课程
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
     * 课程名称
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 课程描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 课时数
     */
    @Column(nullable = false)
    private Integer sessionCount;

    /**
     * 训练类型（基础服从/敏捷训练/行为矫正/社交训练）
     */
    @Column(nullable = false, length = 100)
    private String trainingType;

    /**
     * 适合的宠物类型
     */
    @Column(length = 100)
    private String suitableFor;

    @Override
    public String toString() {
        return title + " (" + sessionCount + "课时)";
    }
}