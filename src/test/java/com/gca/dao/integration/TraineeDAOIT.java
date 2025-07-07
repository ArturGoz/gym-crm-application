package com.gca.dao.integration;

import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.model.Trainee;
import com.gca.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = TraineeDAOImpl.class)
class TraineeDAOIT  extends AbstractDaoIntegrationTest<TraineeDAOImpl> {

    private static final LocalDate BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final String ADDRESS = "Some address";

    @Autowired
    void setDao(TraineeDAOImpl dao) {
        this.dao = dao;
    }

    @Test
    void shouldReturnTraineeById2() throws IOException {
        session.createNativeQuery(readSqlScript("/datasets/test-data.sql"), Void.class)
                .executeUpdate();

        Trainee found = dao.getById(1L);
        System.out.println(found);
        assertNotNull(found);
        assertEquals("finduser", found.getUser().getUsername());
    }

    @Test
    void shouldSuccessfullyCreateTrainee() {
        Trainee trainee = buildTrainee(null, null, "testuser");

        Trainee created = dao.create(trainee);

        assertNotNull(created.getId(), "Created trainee ID should not be null");

        Trainee loaded = dao.getById(created.getId());

        assertNotNull(loaded, "Loaded trainee should not be null");
        assertEquals(ADDRESS, loaded.getAddress());
        assertEquals(BIRTHDAY, loaded.getDateOfBirth());
        assertEquals("testuser", loaded.getUser().getUsername());
    }

    @Test
    void shouldReturnTraineeById() {
        Trainee trainee = buildTrainee(null, null, "finduser");

        Trainee created = dao.create(trainee);
        Trainee found = dao.getById(created.getId());

        assertNotNull(found, "Found trainee should not be null");
        assertEquals("finduser", found.getUser().getUsername());
    }

    @Test
    void shouldUpdateTrainee() {
        Trainee trainee = buildTrainee(null, null, "upduser");
        Trainee created = dao.create(trainee);

        created.setAddress("Updated Address");

        Trainee updated = dao.update(created);

        assertEquals("Updated Address", updated.getAddress());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingTrainee() {
        Trainee nonExisting = buildTrainee(9999L, null, "nonexist");

        assertThrows(RuntimeException.class, () -> {
            dao.update(nonExisting);
        });
    }

    @Test
    void shouldDeleteTraineeById() {
        Trainee trainee = buildTrainee(null, null, "deluser");
        Trainee created = dao.create(trainee);

        dao.delete(created.getId());

        Trainee deleted = dao.getById(created.getId());
        assertNull(deleted, "Deleted trainee should be null");
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
                .firstName("John")
                .lastName("Doe")
                .password("pass")
                .isActive(true)
                .build();
    }
}

