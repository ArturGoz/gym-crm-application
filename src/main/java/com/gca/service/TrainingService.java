package com.gca.service;

import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;

public interface TrainingService {
    TrainingResponse createTraining(TrainingCreateRequest request);

    TrainingResponse getTrainingById(Long id);
}