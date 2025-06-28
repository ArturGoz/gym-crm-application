package com.gca.dao;

import com.gca.model.User;

import java.util.List;

public interface UserDAO<T extends User> {
    T create(T entity);
    T update(T entity);
    T getById(Long id);
    T getByUsername(String username);
    List<String> getAllUsernames();
}
