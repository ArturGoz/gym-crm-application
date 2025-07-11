package com.gca.service;

import com.gca.dto.TrainingFilterDTO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.model.Training;

import java.util.List;

public interface TrainingService {
    TrainingResponse createTraining(TrainingCreateRequest request);

    TrainingResponse getTrainingById(Long id);

    List<Training> getTraineeTrainings(TrainingFilterDTO filter);

    List<Training> getTrainerTrainings(TrainingFilterDTO filter);
}