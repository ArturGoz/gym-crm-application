package com.gca.dao;

import com.gca.model.User;

public interface TraineeDAO {
    User create(User entity);

    User update(User entity);

    User getById(Long id);

    void delete(Long id);
}
