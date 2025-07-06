package com.gca.dao;

import com.gca.dao.impl.TrainingDAOImpl;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TrainingDAOImpl dao;

    @Test
    void shouldSuccessfullyCreateTraining() {
        Training expected = buildTraining();

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        Training actual = dao.create(expected);

        verify(sessionFactory).getCurrentSession();
        verify(session).persist(expected);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getType(), actual.getType());
    }

    @Test
    void shouldReturnTrainingById() {
        Long trainingId = 123L;
        Training expected = buildTraining();
        expected.setId(trainingId);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Training.class, trainingId)).thenReturn(expected);

        Training actual = dao.getById(trainingId);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Training.class, trainingId);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getType(), actual.getType());
    }

    @Test
    void shouldReturnNullIfTrainingByIdNotFound() {
        Long trainingId = 9999L;

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Training.class, trainingId)).thenReturn(null);

        Training actual = dao.getById(trainingId);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Training.class, trainingId);

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
                .user(buildUser())
                .build();
    }

    private User buildUser() {
        return User.builder()
                .id(TRAINEE_USER_ID)
                .username(TRAINEE_USERNAME)
                .firstName(TRAINEE_FIRSTNAME)
                .lastName(TRAINEE_LASTNAME)
                .password(TRAINEE_PASSWORD)
                .isActive(true)
                .build();
    }

    private TrainingType buildTrainingType() {
        return TrainingType.builder()
                .id(TRAINING_TYPE_ID)
                .name(TRAINING_TYPE_NAME)
                .build();
    }
}
