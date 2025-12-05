package com.nan.myschool.service;

import com.nan.myschool.entity.Trainer;
import com.nan.myschool.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(Integer id) {
        return trainerRepository.findById(id).orElse(null);
    }

    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Integer id) {
        trainerRepository.deleteById(id);
    }
}