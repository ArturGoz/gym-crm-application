package com.gca.dao;

import com.gca.model.Trainer;
import com.gca.model.User;

public interface TrainerDAO {
    Trainer create(Trainer entity);

    Trainer update(Trainer entity);

    Trainer getById(Long id);
}
