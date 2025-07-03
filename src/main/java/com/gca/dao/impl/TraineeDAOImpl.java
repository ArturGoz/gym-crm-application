package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDAOImpl extends AbstractUserDAOImpl<Trainee> implements TraineeDAO {
    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
