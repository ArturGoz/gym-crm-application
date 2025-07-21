package com.gca.service;

import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.dto.trainer.TrainerCreateDTO;
import com.gca.dto.trainer.TrainerUpdateRequestDTO;
import com.gca.dto.trainer.TrainerUpdateResponseDTO;
import com.gca.dto.user.UserCredentialsDTO;

import java.util.List;

public interface TrainerService {
    UserCredentialsDTO createTrainer(TrainerCreateDTO request);

    TrainerUpdateResponseDTO updateTrainer(TrainerUpdateRequestDTO request);

    AssignedTrainerDTO getTrainerByUsername(String username);

    List<AssignedTrainerDTO> getUnassignedTrainers(String traineeUsername);
}
