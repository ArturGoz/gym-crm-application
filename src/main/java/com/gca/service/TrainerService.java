package com.gca.service;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;

public interface TrainerService {
    TrainerResponse createTrainer(TrainerCreateRequest request);

    TrainerResponse updateTrainer(TrainerUpdateRequest request);

    TrainerResponse getTrainerById(Long id);
}
