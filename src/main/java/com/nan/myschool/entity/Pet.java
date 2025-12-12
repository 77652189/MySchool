package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Pet Entity - Pet Information
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
     * Pet name
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Pet breed
     */
    @Column(nullable = false, length = 100)
    private String breed;

    /**
     * Pet species (Dog/Cat/Rabbit/etc.)
     */
    @Column(nullable = false, length = 50)
    private String species;

    /**
     * Age in months
     */
    @Column
    private Integer ageInMonths;

    /**
     * Temperament description
     */
    @Column(length = 200)
    private String temperament;

    /**
     * Owner name
     */
    @Column(nullable = false, length = 100)
    private String ownerName;

    /**
     * Owner contact information
     */
    @Column(nullable = false, length = 100)
    private String ownerContact;

    @Override
    public String toString() {
        return name + " (" + breed + ")";
    }

    /**
     * Photo URL
     */
    @Column(name = "photo_url")
    private String photoUrl;  // 存储图片路径

}
