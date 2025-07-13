package com.gca.service;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerDTO;
import com.gca.dto.trainer.TrainerUpdateRequest;

import java.util.List;

public interface TrainerService {
    TrainerDTO createTrainer(TrainerCreateRequest request);

    TrainerDTO updateTrainer(TrainerUpdateRequest request);

    TrainerDTO getTrainerByUsername(String username);

    List<TrainerDTO> getUnassignedTrainers(String traineeUsername);
}
