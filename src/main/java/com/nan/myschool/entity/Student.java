package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Student实体类 - 学生信息
 * 继承User的ID作为主键和外键
 */
@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    /**
     * 学生ID - 主键，同时也是外键指向User表
     */
    @Id
    private Integer studentId;

    /**
     * 通过studentId建立与User表的一对一关系
     * @MapsId 表示使用User的ID作为Student的主键
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private User user;

    /**
     * 学生姓名
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 学生邮箱
     */
    @Column(nullable = false, length = 100)
    private String email;

    /**
     * 专业
     */
    @Column(length = 100)
    private String major;

    /**
     * 年级 - Freshman, Sophomore, Junior, Senior
     */
    @Column(length = 20)
    private String level;

    @Override
    public String toString() {
        return name + " (" + major + ")";
    }

    // 注意：enrolledCourses (List) 不需要在这里定义
    // 它通过Enrollment表查询获得
}