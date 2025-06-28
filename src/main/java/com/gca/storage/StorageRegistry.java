package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

import static com.gca.storage.Namespace.TRAINEE;
import static com.gca.storage.Namespace.TRAINER;
import static com.gca.storage.Namespace.TRAINING;

@Component
public class StorageRegistry {
    private final Map<Namespace, Map<Long, ?>> storageMap = new EnumMap<>(Namespace.class);

    public StorageRegistry(
            Map<Long, Training> trainingStorage,
            Map<Long, Trainer> trainerStorage,
            Map<Long, Trainee> traineeStorage) {
        storageMap.put(TRAINING, trainingStorage);
        storageMap.put(TRAINER, trainerStorage);
        storageMap.put(TRAINEE, traineeStorage);
    }

    @SuppressWarnings("unchecked")
    public <T> Map<Long, T> getStorage(Namespace namespace) {
        return (Map<Long, T>) storageMap.get(namespace);
    }

    @SuppressWarnings("unchecked")
    public void initStorages(InitializedData initData) {
        Map<Long, Training> trainingStorage = (Map<Long, Training>) storageMap.get(TRAINING);
        Map<Long, Trainer> trainerStorage = (Map<Long, Trainer>) storageMap.get(TRAINER);
        Map<Long, Trainee> traineeStorage = (Map<Long, Trainee>) storageMap.get(TRAINEE);

        initData.getTrainings().forEach(t -> trainingStorage.put(t.getId(), t));
        initData.getTrainers().forEach(t -> trainerStorage.put(t.getUserId(), t));
        initData.getTrainees().forEach(t -> traineeStorage.put(t.getUserId(), t));
    }
}

