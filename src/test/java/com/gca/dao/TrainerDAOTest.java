package com.gca.dao;

import com.gca.dao.impl.TrainerDAOImpl;
import com.gca.exception.DaoException;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDAOTest {

    private static final Long USER_ID = 1001L;
    private static final Long TYPE_ID = 991L;

    private static final String USERNAME = "traineruser";
    private static final String FIRSTNAME = "Jane";
    private static final String LASTNAME = "Smith";
    private static final String PASSWORD = "trainerpass";
    private static final String TYPE_NAME = "Fitness";
    private static final boolean IS_ACTIVE = true;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TrainerDAOImpl dao;

    @Test
    void shouldSuccessfullyCreateTrainer() {
        Trainer expected = buildTrainer(1L, USER_ID, USERNAME);

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        Trainer actual = dao.create(expected);

        verify(sessionFactory).getCurrentSession();
        verify(session).persist(expected);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }

    @Test
    void shouldReturnTrainerById() {
        Long trainerId = 10L;
        Trainer expected = buildTrainer(trainerId, 20L, "user10");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Trainer.class, trainerId)).thenReturn(expected);

        Trainer actual = dao.getById(trainerId);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Trainer.class, trainerId);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }

    @Test
    void shouldUpdateTrainer() {
        Long trainerId = 5L;
        Trainer trainer = buildTrainer(trainerId, 105L, "upduser");

        TrainingType newType = TrainingType.builder()
                .id(123L)
                .name("Yoga")
                .build();

        Trainer expected = trainer.toBuilder()
                .specialization(newType)
                .build();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Trainer.class, trainerId)).thenReturn(trainer);
        when(session.merge(expected)).thenReturn(expected);

        Trainer actual = dao.update(expected);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Trainer.class, trainerId);
        verify(session).merge(expected);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingTrainer() {
        Long trainerId = 5L;
        Trainer trainer = buildTrainer(trainerId, 105L, "upduser");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Trainer.class, trainerId)).thenReturn(null);

        assertThrows(DaoException.class, () -> dao.update(trainer));

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Trainer.class, trainerId);
        verify(session, never()).merge(any());
    }

    private Trainer buildTrainer(Long trainerId, Long userId, String username) {
        return Trainer.builder()
                .id(trainerId)
                .user(buildUser(userId, username))
                .specialization(TrainingType.builder()
                        .id(TYPE_ID)
                        .name(TYPE_NAME)
                        .build())
                .build();
    }

    private User buildUser(Long userId, String username) {
        return User.builder()
                .id(userId)
                .username(username)
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();
    }
}
