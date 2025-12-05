package com.nan.myschool.repository;

import com.nan.myschool.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {

}
