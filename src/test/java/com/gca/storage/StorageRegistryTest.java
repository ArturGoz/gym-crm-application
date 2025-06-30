package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageRegistryTest {

    @Test
    void testInitStoragesAndGetStorage() {
        Map<Long, Training> trainingStorage = new HashMap<>();
        Map<Long, Trainer> trainerStorage = new HashMap<>();
        Map<Long, Trainee> traineeStorage = new HashMap<>();

        StorageRegistry registry =
                new StorageRegistry(trainingStorage, trainerStorage, traineeStorage);

        Training training = Training.builder().id(11L).build();
        Trainer trainer = Trainer.builder().userId(22L).build();
        Trainee trainee = Trainee.builder().userId(33L).build();

        InitializedData data = new InitializedData();
        data.getTrainings().add(training);
        data.getTrainers().add(trainer);
        data.getTrainees().add(trainee);

        registry.initStorages(data);

        Map<Long, Training> actualTrainingStorage = registry.getStorage(Namespace.TRAINING);
        Map<Long, Trainer> actualTrainerStorage = registry.getStorage(Namespace.TRAINER);
        Map<Long, Trainee> actualTraineeStorage = registry.getStorage(Namespace.TRAINEE);

        assertEquals(training, actualTrainingStorage.get(11L));
        assertEquals(trainer, actualTrainerStorage.get(22L));
        assertEquals(trainee, actualTraineeStorage.get(33L));
    }
}
