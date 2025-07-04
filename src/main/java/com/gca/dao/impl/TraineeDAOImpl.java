package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Data
public class TraineeDAOImpl implements TraineeDAO {
    protected Map<Long, Trainee> storage;

    @Override
    public Trainee create(Trainee entity) {
        return null;
    }

    @Override
    public Trainee update(Trainee entity) {
        return null;
    }

    @Override
    public Trainee getById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
