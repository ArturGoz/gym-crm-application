package com.gca.dao.impl;

import com.gca.dao.TrainingDAO;
import com.gca.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    @Autowired
    private Map<Long, Training> storage;

    private static Long idCounter = 0L;

    @Override
    public Training create(Training training) {
        Long id = idCounter++;
        training.setId(id);
        storage.put(id, training);
        return training;
    }

    @Override
    public Training getById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Training> getAll() {
        return new ArrayList<>(storage.values());
    }
}
