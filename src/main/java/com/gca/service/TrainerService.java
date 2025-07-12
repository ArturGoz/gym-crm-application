package com.gca.service;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.model.Trainer;

import java.util.List;

public interface TrainerService {
    TrainerResponse createTrainer(TrainerCreateRequest request);

    TrainerResponse updateTrainer(TrainerUpdateRequest request);

    TrainerResponse getTrainerById(Long id);

    TrainerResponse getTrainerByUsername(String username);

    List<Trainer> getUnassignedTrainers(String traineeUsername);
}
