package com.gca.dao.impl;

import com.gca.dao.UserDAO;
import com.gca.model.User;
import com.gca.storage.StorageRegistry;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public abstract class AbstractUserDAOImpl<T extends User> implements UserDAO<T> {
    protected Map<Long, T> storage;

    protected abstract void setStorage(StorageRegistry storageRegistry);

    @Override
    @SuppressWarnings("unchecked")
    public T create(T entity) {
        Long id = getNextId();

        entity = (T) entity.toBuilder()
                .userId(id)
                .build();

        storage.put(id, entity);

        return entity;
    }

    @Override
    public T update(T entity) {
        if (!storage.containsKey(entity.getUserId())) {
            throw new RuntimeException(String.format("%s not found with id: %s",
                    this.getClass().getSimpleName(),
                    entity.getUserId()));
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
    public List<String> getAllUsernames() {
        return storage.values().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    protected Long getNextId() {
        return storage.keySet().stream()
                .mapToLong(Long::longValue)
                .max().orElse(0L) + 1;
    }
}
