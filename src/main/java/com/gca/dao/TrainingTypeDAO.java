package com.gca.dao;

import com.gca.model.TrainingType;

import java.util.List;

public interface TrainingTypeDAO {
    TrainingType getById(Long id);

    TrainingType getByName(String name);

    List<TrainingType> findAllTrainingTypes();
}
