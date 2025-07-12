package com.gca.service;

import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;

import java.util.List;

public interface TrainingService {
    TrainingResponse createTraining(TrainingCreateRequest request);

    TrainingResponse getTrainingById(Long id);

    List<TrainingResponse> getTraineeTrainings(TrainingTraineeCriteriaFilter filter);

    List<TrainingResponse> getTrainerTrainings(TrainingTrainerCriteriaFilter filter);
}