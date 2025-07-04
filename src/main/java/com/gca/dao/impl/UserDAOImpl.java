package com.gca.dao.impl;

import com.gca.dao.UserDAO;
import com.gca.model.User;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class UserDAOImpl implements UserDAO {
    protected Map<Long, User> storage;

    @Override
    @SuppressWarnings("unchecked")
    public User create(User entity) {
        Long id = getNextId();

        entity = entity.toBuilder()
                .id(id)
                .build();

        storage.put(id, entity);

        return entity;
    }

    @Override
    public User update(User entity) {
        if (!storage.containsKey(entity.getId())) {
            throw new RuntimeException(String.format("%s not found with id: %s",
                    this.getClass().getSimpleName(),
                    entity.getId()));
        }
        storage.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public User getById(Long id) {
        return storage.get(id);
    }

    @Override
    public User getByUsername(String username) {
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
