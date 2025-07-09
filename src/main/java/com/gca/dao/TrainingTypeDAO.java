package com.gca.dao;

import com.gca.model.TrainingType;

public interface TrainingTypeDAO {
    TrainingType create(TrainingType training);

    TrainingType getById(Long id);
}
