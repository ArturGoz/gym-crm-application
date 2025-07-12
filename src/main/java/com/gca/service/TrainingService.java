package com.gca.service;

import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.model.Training;

import java.util.List;

public interface TrainingService {
    TrainingResponse createTraining(TrainingCreateRequest request);

    TrainingResponse getTrainingById(Long id);

    List<Training> getTraineeTrainings(TrainingTraineeCriteriaFilter filter);

    List<Training> getTrainerTrainings(TrainingTrainerCriteriaFilter filter);
}