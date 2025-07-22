package com.gca.service;

import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.training.TrainingCreateDTO;
import com.gca.dto.training.TrainingDTO;
import com.gca.model.TrainingType;

import java.util.List;

public interface TrainingService {
    TrainingDTO createTraining(TrainingCreateDTO request);

    List<TrainingDTO> getTraineeTrainings(TrainingTraineeCriteriaFilter filter);

    List<TrainingDTO> getTrainerTrainings(TrainingTrainerCriteriaFilter filter);

    List<TrainingType> getAllTrainingTypes();
}