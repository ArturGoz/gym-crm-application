package com.gca.service;

import com.gca.model.Training;

import java.util.List;

public interface TrainingService {
    Training createTraining(Training training);
    Training getTrainingById(Long id);
    List<Training> getAllTrainings();
/*    List<Training> getTrainingsByTraineeId(Long traineeId);
    List<Training> getTrainingsByTrainerId(Long trainerId);*/
}