package com.gca.dao;

import com.gca.dao.impl.UserDAOImpl;
import com.gca.exception.DaoException;
import com.gca.model.User;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = UserDAOImpl.class)
@DataSet(value = "dataset/user/user-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
public class UserDAOImplTest extends BaseIntegrationTest<UserDAOImpl> {
    private static final Long EXISTING_ID = 1L;
    private static final String EXISTING_USERNAME = "john.doe";

    @Test
    void shouldFindUserById() {
        User expected = sessionFactory.getCurrentSession().find(User.class, EXISTING_ID);

        User actual = dao.getById(EXISTING_ID);

        assertNotNull(actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
    }

    @Test
    void shouldNotFindUserById() {
        User actual = dao.getById(999L);
        assertNull(actual);
    }

    @Test
    void shouldFindUserByUsername() {
        User actual = dao.getByUsername(EXISTING_USERNAME);

        assertNotNull(actual);
        assertEquals(EXISTING_USERNAME, actual.getUsername());
    }

    @Test
    void shouldReturnNullWhenUserNotFoundByUsername() {
        User actual = dao.getByUsername("unknown.user");
        assertNull(actual);
    }

    @Test
    void shouldReturnAllUsernames() {
        List<String> usernames = dao.getAllUsernames();

        assertNotNull(usernames);
        assertTrue(usernames.contains("john.doe"));
        assertTrue(usernames.contains("ghost.user"));
        assertEquals(2, usernames.size());
    }

    @Test
    @DataSet(value = "dataset/user/no-user-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateUser() {
        User newUser = User.builder()
                .firstName("New")
                .lastName("User")
                .username("new.user")
                .password("password")
                .isActive(true)
                .build();

        User actual = dao.create(newUser);

        assertNotNull(actual.getId(), "Created user should have ID");
        assertEquals(newUser.getUsername(), actual.getUsername());
        assertEquals(newUser.getPassword(), actual.getPassword());
        assertEquals(newUser.getIsActive(), actual.getIsActive());
    }

    @Test
    void shouldUpdateUser() {
        User user = sessionFactory.getCurrentSession().find(User.class, EXISTING_ID);
        user.setFirstName("UpdatedName");

        User updated = dao.update(user);

        assertEquals("UpdatedName", updated.getFirstName());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingUser() {
        User nonExisting = User.builder()
                .id(999L)
                .firstName("Ghost")
                .lastName("User")
                .username("ghost.nonexistent")
                .password("pass")
                .isActive(true)
                .build();

        assertThrows(DaoException.class, () -> dao.update(nonExisting));
    }

    @Test
    void shouldDeleteUserById() {
        User existingUser = sessionFactory.getCurrentSession().find(User.class, EXISTING_ID);
        assertNotNull(existingUser);

        dao.delete(existingUser.getId());

        boolean isDeleted = sessionFactory.getCurrentSession()
                .find(User.class, EXISTING_ID) == null;

        assertTrue(isDeleted, "User should be deleted");
    }

    @Test
    void shouldThrowWhenDeletingNonExistingUser() {
        assertThrows(DaoException.class, () -> dao.delete(999L));
    }
}
