package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Enrollment实体类 - 选课记录
 * 实现Student和CourseSection之间的多对多关系
 */
@Entity
@Table(name = "enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_section_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    /**
     * 选课记录ID - 主键，自动生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer enrollmentId;

    /**
     * 学生 - 多对一关系
     * 一个学生可以有多个选课记录
     */
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * 课程章节 - 多对一关系
     * 一个课程章节可以有多个学生选课
     */
    @ManyToOne
    @JoinColumn(name = "course_section_id", nullable = false)
    private CourseSection courseSection;

    /**
     * 成绩 - 例如: A, B+, C 等
     * 初始为null，教师打分后更新
     */
    @Column(length = 5)
    private String grade;
}