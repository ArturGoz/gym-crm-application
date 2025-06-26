package com.gca.dao;

import com.gca.model.Trainee;

public interface TraineeDAO extends UserDAO<Trainee> {
    void delete(Long id);
}
