package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import com.gca.storage.Namespace;
import com.gca.storage.StorageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDAOImpl extends AbstractUserDAOImpl<Trainer> implements TrainerDAO {
    @Autowired
    @Override
    protected void setStorage(StorageRegistry storageRegistry) {
        this.storage = storageRegistry.getStorage(Namespace.TRAINER);
    }
}