package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    protected Map<Long, Trainer> storage;

    @Override
    public Trainer create(Trainer entity) {
        Long id = getNextId();

        entity = entity.toBuilder()
                .id(id)
                .build();

        storage.put(id, entity);

        return entity;
    }

    @Override
    public Trainer update(Trainer entity) {
        if (!storage.containsKey(entity.getId())) {
            throw new RuntimeException(String.format("%s not found with id: %s",
                    this.getClass().getSimpleName(),
                    entity.getId()));
        }
        storage.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public Trainer getById(Long id) {
        return storage.get(id);
    }

    protected Long getNextId() {
        return storage.keySet().stream()
                .mapToLong(Long::longValue)
                .max().orElse(0L) + 1;
    }
}