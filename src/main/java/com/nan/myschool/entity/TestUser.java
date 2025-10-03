package com.nan.myschool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

    @Entity
    @Table(name = "test_user")
    @Getter
    @Setter
public class TestUser {

    // getters and setters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;


    private String email;

    // when database read data, it will use this constructor
    public TestUser() {}

    // when create new user, it will use this constructor
    public TestUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
