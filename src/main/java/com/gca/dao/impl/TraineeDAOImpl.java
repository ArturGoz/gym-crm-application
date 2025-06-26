package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import com.gca.storage.StorageRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.gca.storage.Namespace.TRAINEE;

@Repository
public class TraineeDAOImpl extends AbstractUserDAOImpl<Trainee> implements TraineeDAO {
    @Autowired
    @Override
    protected void setStorage(StorageRegistry storageRegistry) {
        this.storage = storageRegistry.getStorage(TRAINEE);
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
