package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * User实体类 - 系统中所有用户的基础类
 * 对应数据库中的User表
 */
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户ID - 主键，自动生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名 - 用于登录，必须唯一
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码 - 加密存储
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * 角色 - Admin, Instructor, Student
     */
    @Column(nullable = false, length = 20)
    private String role;
}