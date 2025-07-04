package com.gca.dao;

import com.gca.model.Trainee;
import com.gca.model.User;

public interface TraineeDAO {
    Trainee create(Trainee entity);

    Trainee update(Trainee entity);

    Trainee getById(Long id);

    void delete(Long id);
}
