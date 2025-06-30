package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StorageInitializerPostProcessorTest {

    private StorageInitializer storageInitializerMock;
    private StorageInitializerPostProcessor postProcessor;

    @BeforeEach
    void setUp() {
        storageInitializerMock = mock(StorageInitializer.class);
        postProcessor = new StorageInitializerPostProcessor();
        postProcessor.setStorageInitializer(storageInitializerMock);
    }

    @Test
    void testPostProcessAfterInitialization_withStorageRegistry() {
        Map<Long, Training> trainingStorage = new HashMap<>();
        Map<Long, Trainer> trainerStorage = new HashMap<>();
        Map<Long, Trainee> traineeStorage = new HashMap<>();

        StorageRegistry registry = new StorageRegistry(trainingStorage, trainerStorage, traineeStorage);

        Training training = Training.builder().id(1L).build();
        Trainer trainer = Trainer.builder().userId(2L).build();
        Trainee trainee = Trainee.builder().userId(3L).build();

        InitializedData initializedData = new InitializedData();
        initializedData.getTrainings().add(training);
        initializedData.getTrainers().add(trainer);
        initializedData.getTrainees().add(trainee);

        when(storageInitializerMock.initializeData()).thenReturn(initializedData);

        Object result = postProcessor.postProcessAfterInitialization(registry, "storageRegistry");

        assertSame(registry, result);
        assertEquals(training, trainingStorage.get(1L));
        assertEquals(trainer, trainerStorage.get(2L));
        assertEquals(trainee, traineeStorage.get(3L));
    }

    @Test
    void testPostProcessAfterInitialization_withNonRegistryBean() {
        Object someBean = new Object();
        Object result = postProcessor.postProcessAfterInitialization(someBean, "notARegistry");

        assertSame(someBean, result);
        verify(storageInitializerMock, never()).initializeData();
    }
}
