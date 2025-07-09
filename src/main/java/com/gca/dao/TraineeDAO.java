package com.gca.dao;

import com.gca.model.Trainee;

public interface TraineeDAO {
    Trainee create(Trainee entity);

    Trainee update(Trainee entity);

    Trainee getById(Long id);

    Trainee findByUsername(String username);

    void deleteById(Long id);

    void deleteByUsername(String username);
}
