package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private Map<Long, Trainer> storage;
    private static Long idCounter = 0L;

    @Autowired
    public void setStorage(Map<Long, Trainer> storage) {
        this.storage = storage;
    }

    @Override
    public Trainer create(Trainer trainer) {
        Long id = idCounter++;
        trainer.setUserId(id);
        storage.put(id, trainer);
        return trainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        if (storage.containsKey(trainer.getUserId())) {
            storage.put(trainer.getUserId(), trainer);
            return trainer;
        } else {
            throw new RuntimeException("Trainer not found");
        }
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public Trainer getById(Long id) {
        return storage.get(id);
    }

    @Override
    public Trainer getByUsername(String username) {
        for (Trainer trainer : storage.values()) {
            if (trainer.getUsername().equals(username)) {
                return trainer;
            }
        }
        return null;
    }

    @Override
    public List<Trainer> getAll() {
        return new ArrayList<>(storage.values());
    }
}
