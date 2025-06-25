package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class TrainerDAOImpl extends AbstractUserDAOImpl<Trainer> implements TrainerDAO {
    @Autowired
    @Override
    protected void setStorage(Map<Long, Trainer> storage) {
        this.storage = storage;
    }
}