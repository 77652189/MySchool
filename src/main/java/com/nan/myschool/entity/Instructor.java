package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Instructor实体类 - 教师信息
 * 继承User的ID作为主键和外键
 */
@Entity
@Table(name = "instructor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    /**
     * 教师ID - 主键，同时也是外键指向User表
     */
    @Id
    private Integer instructorId;

    /**
     * 通过instructorId建立与User表的一对一关系
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "instructor_id")
    private User user;

    /**
     * 教师姓名
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 所属院系
     */
    @Column(nullable = false, length = 100)
    private String department;

    @Override
    public String toString() {
        return name + " - " + department;
    }


    // 注意：assignedSections (List) 不需要在这里定义
    // 它通过CourseSection.instructorId查询获得
}