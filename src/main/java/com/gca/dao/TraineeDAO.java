package com.gca.dao;

import com.gca.model.Trainee;
import com.gca.model.Trainer;

public interface TraineeDAO {
    Trainee create(Trainee entity);

    Trainee update(Trainee entity);

    Trainee getById(Long id);

    Trainee getTraineeByUserId(Long userId);

    void delete(Long id);
}
