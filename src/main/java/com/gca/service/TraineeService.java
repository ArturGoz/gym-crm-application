package com.gca.service;

import com.gca.model.Trainee;

import java.util.List;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(Long id);
    Trainee getTraineeById(Long id);
    Trainee getTraineeByUsername(String username);
    List<Trainee> getAllTrainees();
}
