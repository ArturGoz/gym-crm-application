package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Component
public class StorageRegistry {
    private final Map<Namespace, Map<Long, ?>> storageMap = new EnumMap<>(Namespace.class);

    public StorageRegistry() {
        storageMap.put(Namespace.TRAINING, new HashMap<Long, Training>());
        storageMap.put(Namespace.TRAINER, new HashMap<Long, Trainer>());
        storageMap.put(Namespace.TRAINEE, new HashMap<Long, Trainee>());
    }

    @SuppressWarnings("unchecked")
    public <T> Map<Long, T> getStorage(Namespace namespace) {
        return (Map<Long, T>) storageMap.get(namespace);
    }
}

