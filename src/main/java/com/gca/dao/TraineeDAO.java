package com.gca.dao;

import com.gca.model.Trainee;

public interface TraineeDAO {
    Trainee create(Trainee entity);

    Trainee update(Trainee entity);

    Trainee getById(Long id);

    void delete(Long id);
}
