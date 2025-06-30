package com.gca.dao;

import com.gca.dao.impl.TrainingDAOImpl;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainingDAOTest {

    private static final Long TRAINER_ID = 1L;
    private static final Long TRAINEE_ID = 2L;
    private static final LocalDate TRAINING_DATE = LocalDate.of(2025, 6, 27);
    private static final Duration TRAINING_DURATION = Duration.ofHours(1);
    private static final String TRAINING_NAME = "Morning Yoga";
    private static final TrainingType TRAINING_TYPE = new TrainingType("Yoga");

    private TrainingDAOImpl dao;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Training> trainingStorage;

    @BeforeEach
    void setUp() {
        trainingStorage = new HashMap<>();
        storageRegistryMock = mock(StorageRegistry.class);
        when(storageRegistryMock.getStorage(any()))
                .thenReturn((Map) trainingStorage);

        dao = new TrainingDAOImpl();
        dao.setStorage(storageRegistryMock);
    }

    @Test
    void testCreate() {
        Training expected = buildTraining();

        Training actual = dao.create(expected);

        assertNotNull(actual.getId());
        assertEquals(expected.getTrainerId(), actual.getTrainerId());
        assertEquals(expected.getTraineeId(), actual.getTraineeId());
        assertEquals(expected.getTrainingDate(), actual.getTrainingDate());
        assertEquals(expected.getTrainingDuration(), actual.getTrainingDuration());
        assertEquals(expected.getTrainingName(), actual.getTrainingName());
        assertEquals(expected.getTrainingType(), actual.getTrainingType());
        assertEquals(actual, trainingStorage.get(actual.getId()));
    }

    @Test
    void testGetById() {
        Training expected = buildTraining();
        Training created = dao.create(expected);

        Training actual = dao.getById(created.getId());

        assertNotNull(actual);
        assertEquals(created, actual);
        assertEquals(created.getId(), actual.getId());
        assertEquals(created.getTrainerId(), actual.getTrainerId());
        assertEquals(created.getTraineeId(), actual.getTraineeId());
        assertEquals(created.getTrainingDate(), actual.getTrainingDate());
    }

    @Test
    void testCreateAssignsUniqueIds() {
        Training training1 = buildTraining("T1");
        Training training2 = buildTraining("T2");

        Training actual1 = dao.create(training1);
        Training actual2 = dao.create(training2);

        assertNotEquals(actual1.getId(), actual2.getId());
    }

    @Test
    void testGetByIdReturnsNullIfNotFound() {
        Training actual = dao.getById(12345L);

        assertNull(actual);
    }

    @Test
    void testEqualsAndHashCodeAndToString() {
        TrainingDAOImpl dao1 = new TrainingDAOImpl();
        TrainingDAOImpl dao2 = new TrainingDAOImpl();

        dao1.setStorage(storageRegistryMock);
        dao2.setStorage(storageRegistryMock);

        assertEquals(dao1, dao1);
        assertEquals(dao1, dao2);
        assertEquals(dao2, dao1);
        assertEquals(dao1.hashCode(), dao2.hashCode());
        assertEquals(dao1, dao2);
        assertNotNull(dao1.toString());
    }

    private Training buildTraining() {
        return Training.builder()
                .trainerId(TRAINER_ID)
                .traineeId(TRAINEE_ID)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(TRAINING_DURATION)
                .trainingName(TRAINING_NAME)
                .trainingType(TRAINING_TYPE)
                .build();
    }

    private Training buildTraining(String trainingName) {
        return Training.builder()
                .trainerId(TRAINER_ID)
                .traineeId(TRAINEE_ID)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(TRAINING_DURATION)
                .trainingName(trainingName)
                .trainingType(TRAINING_TYPE)
                .build();
    }
}
