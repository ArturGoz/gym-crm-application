package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class TraineeDAOImpl extends AbstractUserDAOImpl<Trainee> implements TraineeDAO {
    @Autowired
    @Override
    protected void setStorage(Map<Long, Trainee> storage) {
        this.storage = storage;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
