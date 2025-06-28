package com.gca.service;

import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;

import java.util.List;

public interface TrainingService {
    TrainingResponse createTraining(TrainingCreateRequest request);
    TrainingResponse getTrainingById(Long id);
    List<TrainingResponse> getAllTrainings();
}