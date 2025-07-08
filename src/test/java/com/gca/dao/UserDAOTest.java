package com.gca.dao;

import com.gca.dao.impl.UserDAOImpl;
import com.gca.exception.DaoException;
import com.gca.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDAOTest extends AbstractDAOTest {

    private static final Long USER_ID = 1001L;
    private static final String USERNAME = "testuser";
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String PASSWORD = "password";
    private static final boolean IS_ACTIVE = true;

    @InjectMocks
    private UserDAOImpl dao;

    @Test
    void shouldSuccessfullyCreateUser() {
        User expected = buildUser();

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        User actual = dao.create(expected);

        verify(sessionFactory).getCurrentSession();
        verify(session).persist(expected);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnUserById() {
        User expected = buildUser();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(User.class, USER_ID)).thenReturn(expected);

        User actual = dao.getById(USER_ID);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(User.class, USER_ID);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateUser() {
        User existing = buildUser();
        User expected = existing.toBuilder()
                .firstName("Updated")
                .build();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(User.class, USER_ID)).thenReturn(existing);
        when(session.merge(expected)).thenReturn(expected);

        User actual = dao.update(expected);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(User.class, USER_ID);
        verify(session).merge(expected);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingUser() {
        User nonExisting = buildUser();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(User.class, USER_ID)).thenReturn(null);

        assertThrows(DaoException.class, () -> dao.update(nonExisting));

        verify(sessionFactory).getCurrentSession();
        verify(session).find(User.class, USER_ID);
        verify(session, never()).merge(any());
    }

    @Test
    void shouldDeleteUser() {
        User existing = buildUser();

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(User.class, USER_ID)).thenReturn(existing);

        dao.delete(USER_ID);

        verify(sessionFactory).getCurrentSession();
        verify(session).find(User.class, USER_ID);
        verify(session).remove(existing);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingUser() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.find(User.class, USER_ID)).thenReturn(null);

        assertThrows(DaoException.class, () -> dao.delete(USER_ID));

        verify(sessionFactory).getCurrentSession();
        verify(session).find(User.class, USER_ID);
        verify(session, never()).remove(any());
    }

    @Test
    void shouldReturnUserByUsername() {
        User expected = buildUser();

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        var query = mock(org.hibernate.query.Query.class);

        when(session.createQuery(
                "FROM User u WHERE u.username = :username", User.class))
                .thenReturn(query);

        when(query.setParameter("username", USERNAME)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(expected);

        User actual = dao.getByUsername(USERNAME);

        verify(sessionFactory).getCurrentSession();
        verify(session).createQuery(anyString(), eq(User.class));
        verify(query).setParameter("username", USERNAME);
        verify(query).uniqueResult();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnAllUsernames() {
        List<String> usernames = List.of("user1", "user2", "user3");

        var query = mock(org.hibernate.query.Query.class);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery(anyString(), eq(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(usernames);

        List<String> actual = dao.getAllUsernames();

        verify(sessionFactory).getCurrentSession();
        verify(session).createQuery(anyString(), eq(String.class));
        verify(query).getResultList();

        assertEquals(usernames, actual);
    }

    private User buildUser() {
        return User.builder()
                .id(UserDAOTest.USER_ID)
                .username(UserDAOTest.USERNAME)
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();
    }
}
