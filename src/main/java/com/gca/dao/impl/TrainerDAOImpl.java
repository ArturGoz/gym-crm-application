package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import com.gca.storage.StorageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.gca.storage.Namespace.TRAINER;

@Repository
public class TrainerDAOImpl extends AbstractUserDAOImpl<Trainer> implements TrainerDAO {
    @Autowired
    @Override
    protected void setStorage(StorageRegistry storageRegistry) {
        this.storage = storageRegistry.getStorage(TRAINER);
    }
}