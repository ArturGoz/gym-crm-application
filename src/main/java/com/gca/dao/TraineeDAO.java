package com.gca.dao;

import com.gca.model.Trainee;

import java.util.List;

public interface TraineeDAO {
    Trainee create(Trainee entity);

    Trainee update(Trainee entity);

    Trainee getById(Long id);

    Trainee findByUsername(String username);

    Trainee updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames);

    void deleteById(Long id);

    void deleteByUsername(String username);
}
