package com.gca.dao.impl;

import com.gca.dao.TrainingDAO;
import com.gca.model.Training;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private static Long idCounter = 0L;

    private Map<Long, Training> storage;

    @Override
    public Training create(Training training) {
        Long id = idCounter++;

        training = training.toBuilder()
                .id(id)
                .build();

        storage.put(id, training);

        return training;
    }

    @Override
    public Training getById(Long id) {
        return storage.get(id);
    }
}
