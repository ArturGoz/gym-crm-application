package com.gca.dao;

import com.gca.model.Trainer;

import java.util.List;

public interface TrainerDAO {
    Trainer create(Trainer entity);

    Trainer update(Trainer entity);

    Trainer getById(Long id);

    Trainer findByUsername(String username);

    List<Trainer> getAllTrainers();
}
