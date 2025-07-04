package com.gca.dao;

import com.gca.model.User;

public interface TrainerDAO {
    User create(User entity);

    User update(User entity);

    User getById(Long id);
}
