package com.gca.dao;

import com.gca.model.Training;

import java.util.List;

public interface TrainingDAO {
    Training create(Training training);
    Training getById(Long id);
    List<Training> getAll();
}
