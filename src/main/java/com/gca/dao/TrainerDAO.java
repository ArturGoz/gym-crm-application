package com.gca.dao;

import com.gca.model.Trainer;

import java.util.List;

public interface TrainerDAO {
    Trainer create(Trainer trainer);
    Trainer update(Trainer trainer);
    void delete(Long id);
    Trainer getById(Long id);
    Trainer getByUsername(String username);
    List<Trainer> getAll();
}
