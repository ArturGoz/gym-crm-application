package com.gca.dao;

import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.exception.DaoException;
import com.gca.model.Trainee;
import com.gca.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeDAOTest extends AbstractDAOTest {

    private static final Long USER_ID = 111L;
    private static final String USERNAME = "testuser";
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String PASSWORD = "pass";
    private static final LocalDate BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final String ADDRESS = "Some address";
    private static final boolean IS_ACTIVE = true;

    @InjectMocks
    private TraineeDAOImpl dao;

    @Test
    void shouldSuccessfullyCreateTrainee() {
        Trainee expected = buildTrainee(1L, USER_ID, USERNAME);

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        Trainee actual = dao.create(expected);

        verify(sessionFactory).getCurrentSession();
        verify(session).persist(expected);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
    }

    @Test
    void shouldReturnTraineeById() {
        Long traineeId = 10L;
        Trainee expected = buildTrainee(traineeId, 20L, "user10");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Trainee.class, traineeId)).thenReturn(expected);

        Trainee actual = dao.getById(traineeId);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Trainee.class, traineeId);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
    }

    @Test
    void shouldUpdateTrainee() {
        Long traineeId = 5L;
        Trainee expected = buildTrainee(traineeId, 105L, "upduser");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Trainee.class, traineeId)).thenReturn(expected);
        when(session.merge(expected)).thenReturn(expected);

        Trainee actual = dao.update(expected);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Trainee.class, traineeId);
        verify(session).merge(expected);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingTrainee() {
        Long traineeId = 5L;
        Trainee trainee = buildTrainee(traineeId, 105L, "upduser");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Trainee.class, traineeId)).thenReturn(null);

        assertThrows(DaoException.class, () -> dao.update(trainee));

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Trainee.class, traineeId);
        verify(session, never()).merge(any());
    }

    @Test
    void shouldDeleteTraineeById() {
        Long traineeId = 6L;
        Trainee trainee = buildTrainee(traineeId, 106L, "deluser");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(Trainee.class, traineeId)).thenReturn(trainee);

        dao.delete(traineeId);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(Trainee.class, traineeId);
        verify(session).remove(trainee);
    }

    private Trainee buildTrainee(Long traineeId, Long userId, String username) {
        return Trainee.builder()
                .id(traineeId)
                .dateOfBirth(BIRTHDAY)
                .address(ADDRESS)
                .user(buildUser(userId, username))
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
