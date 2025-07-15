package com.gca.service;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerDTO;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateDTO;
import com.gca.dto.user.UserCreationDTO;

import java.util.List;

public interface TrainerService {
    UserCreationDTO createTrainer(TrainerCreateRequest request);

    TrainerUpdateDTO updateTrainer(TrainerUpdateRequest request);

    TrainerDTO getTrainerByUsername(String username);

    List<TrainerDTO> getUnassignedTrainers(String traineeUsername);
}
