package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import com.gca.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    protected Map<Long, Trainee> storage;

    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getAllUsernames() {
        return List.of();
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
