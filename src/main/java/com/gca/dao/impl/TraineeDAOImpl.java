package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private Map<Long, Trainee> storage;
    private static Long idCounter = 0L;

    @Autowired
    public void setStorage(Map<Long, Trainee> storage) {
        this.storage = storage;
    }

    @Override
    public Trainee create(Trainee trainee) {
        Long id = idCounter++;
        trainee.setUserId(id);
        storage.put(id, trainee);
        return trainee;
    }

    @Override
    public Trainee update(Trainee trainee) {
        if (storage.containsKey(trainee.getUserId())) {
            storage.put(trainee.getUserId(), trainee);
            return trainee;
        } else {
            throw new RuntimeException("Trainee not found");
        }
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public Trainee getById(Long id) {
        return storage.get(id);
    }

    @Override
    public Trainee getByUsername(String username) {
        for (Trainee trainee : storage.values()) {
            if (trainee.getUsername().equals(username)) {
                return trainee;
            }
        }
        return null;
    }

    @Override
    public List<Trainee> getAll() {
        return new ArrayList<>(storage.values());
    }
}
