package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class StorageRegistry {
    private final Map<Namespace, Map<Long, ?>> storageMap = new EnumMap<>(Namespace.class);

    public StorageRegistry(
            Map<Long, Training> trainingStorage,
            Map<Long, Trainer> trainerStorage,
            Map<Long, Trainee> traineeStorage
    ) {
        storageMap.put(Namespace.TRAINING, trainingStorage);
        storageMap.put(Namespace.TRAINER, trainerStorage);
        storageMap.put(Namespace.TRAINEE, traineeStorage);
    }

    @SuppressWarnings("unchecked")
    public <T> Map<Long, T> getStorage(Namespace namespace) {
        return (Map<Long, T>) storageMap.get(namespace);
    }

    @SuppressWarnings("unchecked")
    public void initStorages(InitializedData initData) {
        Map<Long, Training> trainingStorage = (Map<Long, Training>) storageMap.get(Namespace.TRAINING);
        Map<Long, Trainer> trainerStorage = (Map<Long, Trainer>) storageMap.get(Namespace.TRAINER);
        Map<Long, Trainee> traineeStorage = (Map<Long, Trainee>) storageMap.get(Namespace.TRAINEE);

        initData.getTrainings().forEach(t -> trainingStorage.put(t.getId(), t));
        initData.getTrainers().forEach(t -> trainerStorage.put(t.getUserId(), t));
        initData.getTrainees().forEach(t -> traineeStorage.put(t.getUserId(), t));
    }
}

