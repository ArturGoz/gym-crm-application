package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Data
public class TrainerDAOImpl implements TrainerDAO {
    protected Map<Long, Trainer> storage;

    @Override
    public Trainer create(Trainer entity) {
        return null;
    }

    @Override
    public Trainer update(Trainer entity) {
        return null;
    }

    @Override
    public Trainer getById(Long id) {
        return null;
    }
}