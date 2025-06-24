package com.gca.dao;

import com.gca.model.Trainee;

import java.util.List;

public interface TraineeDAO {
    Trainee create(Trainee trainee);
    Trainee update(Trainee trainee);
    void delete(Long id);
    Trainee getById(Long id);
    Trainee getByUsername(String username);
    List<Trainee> getAll();
}
