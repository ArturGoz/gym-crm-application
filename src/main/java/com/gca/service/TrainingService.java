package com.gca.service;

import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingDTO;

import java.util.List;

public interface TrainingService {
    TrainingDTO createTraining(TrainingCreateRequest request);

    List<TrainingDTO> getTraineeTrainings(TrainingTraineeCriteriaFilter filter);

    List<TrainingDTO> getTrainerTrainings(TrainingTrainerCriteriaFilter filter);
}