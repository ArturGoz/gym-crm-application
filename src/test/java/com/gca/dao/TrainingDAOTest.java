package com.gca.dao;

import com.gca.dao.impl.TrainingDAOImpl;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrainingDAOTest {

    private static final Long TRAINER_ID = 1L;
    private static final String TRAINER_USERNAME = "traineruser";
    private static final String TRAINER_FIRSTNAME = "Jane";
    private static final String TRAINER_LASTNAME = "Smith";
    private static final String TRAINER_PASSWORD = "trainerpass";
    private static final Long TRAINER_USER_ID = 1001L;

    private static final Long TRAINEE_ID = 2L;
    private static final String TRAINEE_USERNAME = "traineeuser";
    private static final String TRAINEE_FIRSTNAME = "Ivan";
    private static final String TRAINEE_LASTNAME = "Ivanov";
    private static final String TRAINEE_PASSWORD = "traineepass";
    private static final Long TRAINEE_USER_ID = 1002L;

    private static final LocalDate TRAINING_DATE = LocalDate.of(2025, 6, 27);
    private static final Duration TRAINING_DURATION = Duration.ofHours(1);
    private static final String TRAINING_NAME = "Morning Yoga";
    private static final Long TRAINING_DURATION_MINUTES = TRAINING_DURATION.toMinutes();

    private static final Long TRAINING_TYPE_ID = 999L;
    private static final String TRAINING_TYPE_NAME = "Yoga";

    private TrainingDAOImpl dao;

    private Map<Long, Training> trainingStorage;

    @BeforeEach
    void setUp() {
        trainingStorage = new HashMap<>();
        dao = new TrainingDAOImpl();

        ReflectionTestUtils.setField(dao, "storage", trainingStorage);
    }

    @Test
    void shouldSuccessfullyCreateTraining() {
        Training expected = buildTraining();

        Training actual = dao.create(expected);

        assertNotNull(actual.getId());

        assertNotNull(actual.getTrainer());
        assertEquals(expected.getTrainer().getId(), actual.getTrainer().getId());
        assertEquals(expected.getTrainer().getUser().getId(), actual.getTrainer().getUser().getId());

        assertNotNull(actual.getTrainee());
        assertEquals(expected.getTrainee().getId(), actual.getTrainee().getId());
        assertEquals(expected.getTrainee().getUser().getId(), actual.getTrainee().getUser().getId());

        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getName(), actual.getName());

        assertNotNull(actual.getType());
        assertEquals(expected.getType().getId(), actual.getType().getId());
        assertEquals(expected.getType().getName(), actual.getType().getName());

        assertEquals(actual, trainingStorage.get(actual.getId()));
    }

    @Test
    void shouldReturnTrainingById() {
        Training expected = buildTraining();
        Training created = dao.create(expected);

        Training actual = dao.getById(created.getId());

        assertNotNull(actual);
        assertEquals(created, actual);
        assertEquals(created.getId(), actual.getId());
        assertEquals(created.getTrainer().getId(), actual.getTrainer().getId());
        assertEquals(created.getTrainee().getId(), actual.getTrainee().getId());
        assertEquals(created.getDate(), actual.getDate());
    }

    @Test
    void shouldAssignUniqueIdsForEachCreatedTraining() {
        Training training1 = buildTraining("T1");
        Training training2 = buildTraining("T2");

        Training actual1 = dao.create(training1);
        Training actual2 = dao.create(training2);

        assertNotEquals(actual1.getId(), actual2.getId());
    }

    @Test
    void shouldReturnNullIfTrainingByIdNotFound() {
        Training actual = dao.getById(12345L);

        assertNull(actual);
    }

    private Training buildTraining() {
        return Training.builder()
                .trainer(buildTrainer())
                .trainee(buildTrainee())
                .date(TRAINING_DATE)
                .duration(TRAINING_DURATION_MINUTES)
                .name(TRAINING_NAME)
                .type(buildTrainingType())
                .build();
    }

    private Training buildTraining(String trainingName) {
        return Training.builder()
                .trainer(buildTrainer())
                .trainee(buildTrainee())
                .date(TRAINING_DATE)
                .duration(TRAINING_DURATION_MINUTES)
                .name(trainingName)
                .type(buildTrainingType())
                .build();
    }

    private Trainer buildTrainer() {
        return Trainer.builder()
                .id(TRAINER_ID)
                .user(User.builder()
                        .id(TRAINER_USER_ID)
                        .username(TRAINER_USERNAME)
                        .firstName(TRAINER_FIRSTNAME)
                        .lastName(TRAINER_LASTNAME)
                        .password(TRAINER_PASSWORD)
                        .isActive(true)
                        .build())
                .build();
    }

    private Trainee buildTrainee() {
        return Trainee.builder()
                .id(TRAINEE_ID)
                .user(User.builder()
                        .id(TRAINEE_USER_ID)
                        .username(TRAINEE_USERNAME)
                        .firstName(TRAINEE_FIRSTNAME)
                        .lastName(TRAINEE_LASTNAME)
                        .password(TRAINEE_PASSWORD)
                        .isActive(true)
                        .build())
                .build();
    }

    private TrainingType buildTrainingType() {
        return TrainingType.builder()
                .id(TRAINING_TYPE_ID)
                .name(TRAINING_TYPE_NAME)
                .build();
    }
}
