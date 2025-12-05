package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Trainer实体类 - 训练师信息
 */
@Entity
@Table(name = "trainer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    private Integer trainerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "trainer_id")
    private User user;

    /**
     * 训练师姓名
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 专业领域（服从训练/敏捷训练/行为矫正等）
     */
    @Column(nullable = false, length = 100)
    private String specialization;

    /**
     * 认证级别
     */
    @Column(length = 50)
    private String certificationLevel;

    @Override
    public String toString() {
        return name + " - " + specialization;
    }
}