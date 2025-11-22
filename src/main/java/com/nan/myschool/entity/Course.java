package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Course实体类 - 课程信息
 */
@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    /**
     * 课程ID - 主键，自动生成
     */
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
     * 学分
     */
    @Column(nullable = false)
    private Integer credits;

    /**
     * 开课院系
     */
    @Column(nullable = false, length = 100)
    private String department;

    @Override
    public String toString() {
        return title + " (" + credits + "学分)";
    }

}