package com.gca.dao;

import com.gca.model.User;

import java.util.List;

public interface UserDAO {
    User create(User entity);

    User update(User entity);

    User getById(Long id);

    void delete(Long id);

    User getByUsername(String username);

    List<String> getAllUsernames();
}
