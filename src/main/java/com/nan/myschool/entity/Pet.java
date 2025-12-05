package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Pet实体类 - 宠物信息
 */
@Entity
@Table(name = "pet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    private Integer petId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pet_id")
    private User user;

    /**
     * 宠物名字
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 宠物品种
     */
    @Column(nullable = false, length = 100)
    private String breed;

    /**
     * 宠物类型（狗/猫/兔子等）
     */
    @Column(nullable = false, length = 50)
    private String species;

    /**
     * 年龄（月）
     */
    @Column
    private Integer ageInMonths;

    /**
     * 性格描述
     */
    @Column(length = 200)
    private String temperament;

    /**
     * 主人姓名
     */
    @Column(nullable = false, length = 100)
    private String ownerName;

    /**
     * 主人联系方式
     */
    @Column(nullable = false, length = 100)
    private String ownerContact;

    @Override
    public String toString() {
        return name + " (" + breed + ")";
    }
}