package com.nan.myschool.repository;

import com.nan.myschool.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
}
