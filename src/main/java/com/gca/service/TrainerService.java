package com.gca.service;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateResponse;
import com.gca.dto.user.UserCreationResponse;

import java.util.List;

public interface TrainerService {
    UserCreationResponse createTrainer(TrainerCreateRequest request);

    TrainerUpdateResponse updateTrainer(TrainerUpdateRequest request);

    TrainerResponse getTrainerByUsername(String username);

    List<TrainerResponse> getUnassignedTrainers(String traineeUsername);
}
