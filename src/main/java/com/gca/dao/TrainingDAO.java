package com.gca.dao;

import com.gca.model.Training;

public interface TrainingDAO {
    Training create(Training training);

    Training getById(Long id);
}
