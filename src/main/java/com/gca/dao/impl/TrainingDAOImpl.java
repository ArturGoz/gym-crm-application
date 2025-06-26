package com.gca.dao.impl;

import com.gca.dao.TrainingDAO;
import com.gca.model.Training;
import com.gca.storage.StorageRegistry;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gca.storage.Namespace.TRAINING;

@Repository
@Data
public class TrainingDAOImpl implements TrainingDAO {
    private static Long idCounter = 0L;

    private Map<Long, Training> storage;

    @Autowired
    public void setStorage(StorageRegistry storageRegistry) {
        this.storage = storageRegistry.getStorage(TRAINING);
    }

    @Override
    public Training create(Training training) {
        Long id = idCounter++;
        training.setId(id);
        storage.put(id, training);

        return training;
    }

    @Override
    public Training getById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Training> getAll() {
        return new ArrayList<>(storage.values());
    }
}
