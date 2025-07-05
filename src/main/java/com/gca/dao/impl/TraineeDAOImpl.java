package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    protected Map<Long, Trainee> storage;

    @Override
    public Trainee create(Trainee entity) {
        Long id = getNextId();

        entity = entity.toBuilder()
                .id(id)
                .build();

        storage.put(id, entity);

        return entity;
    }

    @Override
    public Trainee update(Trainee entity) {
        if (!storage.containsKey(entity.getId())) {
            throw new RuntimeException(String.format("%s not found with id: %s",
                    this.getClass().getSimpleName(),
                    entity.getId()));
        }
        storage.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public Trainee getById(Long id) {
        return storage.get(id);
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    protected Long getNextId() {
        return storage.keySet().stream()
                .mapToLong(Long::longValue)
                .max().orElse(0L) + 1;
    }
}
