package com.nan.myschool.service;

import com.nan.myschool.entity.Pet;
import com.nan.myschool.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(Integer id) {
        return petRepository.findById(id).orElse(null);
    }

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    public void deletePet(Integer id) {
        petRepository.deleteById(id);
    }
}