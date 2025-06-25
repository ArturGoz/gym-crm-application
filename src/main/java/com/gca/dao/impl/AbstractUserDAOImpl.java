package com.gca.dao.impl;

import com.gca.dao.UserDAO;
import com.gca.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public abstract class AbstractUserDAOImpl<T extends User> implements UserDAO<T> {
    protected Map<Long, T> storage;

    protected abstract void setStorage(Map<Long, T> storage);

    @Override
    public T create(T entity) {
        Long id = getNextId();
        entity.setUserId(id);
        storage.put(id, entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        if (!storage.containsKey(entity.getUserId())) {
            throw new RuntimeException(this.getClass().getSimpleName() + " not found with id: " + entity.getUserId());
        }
        storage.put(entity.getUserId(), entity);
        return entity;
    }

    @Override
    public T getById(Long id) {
        return storage.get(id);
    }

    @Override
    public T getByUsername(String username) {
        return storage.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    protected Long getNextId() {
        return storage.keySet().stream()
                .mapToLong(Long::longValue)
                .max().orElse(0L) + 1;
    }
}
