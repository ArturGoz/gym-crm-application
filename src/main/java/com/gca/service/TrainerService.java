package com.gca.service;

import com.gca.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    Trainer getTrainerById(Long id);
    Trainer getTrainerByUsername(String username);
    List<Trainer> getAllTrainers();
}
