package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import com.gca.storage.Namespace;
import com.gca.storage.StorageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDAOImpl extends AbstractUserDAOImpl<Trainee> implements TraineeDAO {
    @Autowired
    @Override
    protected void setStorage(StorageRegistry storageRegistry) {
        this.storage = storageRegistry.getStorage(Namespace.TRAINEE);
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
