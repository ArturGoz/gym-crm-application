package com.gca.dao;

import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.model.Trainee;
import com.gca.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TraineeDAOTest {

    private static final Long STATIC_USER_ID = 111L;
    private static final String STATIC_USERNAME = "testuser";
    private static final String STATIC_FIRSTNAME = "John";
    private static final String STATIC_LASTNAME = "Doe";
    private static final String STATIC_PASSWORD = "pass";
    private static final LocalDate STATIC_BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final String STATIC_ADDRESS = "Some address";
    private static final boolean STATIC_IS_ACTIVE = true;

    private Map<Long, Trainee> traineeStorage;

    private TraineeDAOImpl dao;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        dao = new TraineeDAOImpl();

        ReflectionTestUtils.setField(dao, "storage", traineeStorage);
    }

    @Test
    void shouldSuccessfullyCreateTrainee() {
        Trainee expected = buildTrainee(1L, STATIC_USER_ID, STATIC_USERNAME);

        Trainee actual = dao.create(expected);

        assertNotNull(actual.getId());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getAddress(), actual.getAddress());

        assertNotNull(actual.getUser());
        assertEquals(expected.getUser().getId(), actual.getUser().getId());
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
        assertEquals(expected.getUser().getLastName(), actual.getUser().getLastName());
        assertEquals(expected.getUser().getPassword(), actual.getUser().getPassword());
        assertEquals(expected.getUser().getIsActive(), actual.getUser().getIsActive());

        assertEquals(actual, traineeStorage.get(expected.getId()));
    }

    @Test
    void shouldReturnTraineeById() {
        Long traineeId = 10L;
        Long userId = 20L;
        String username = "user10";
        Trainee expected = buildTrainee(traineeId, userId, username);

        traineeStorage.put(traineeId, expected);

        Trainee actual = dao.getById(traineeId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
    }

    @Test
    void shouldUpdateTrainee() {
        Long traineeId = 5L;
        Long userId = 105L;
        String username = "upduser";
        Trainee trainee = buildTrainee(traineeId, userId, username);

        traineeStorage.put(traineeId, trainee);

        Trainee expected = trainee.toBuilder()
                .address("new address")
                .build();

        Trainee actual = dao.update(expected);

        assertEquals("new address", actual.getAddress());
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteTraineeById() {
        Long traineeId = 6L;
        Long userId = 106L;
        String username = "deluser";
        Trainee trainee = buildTrainee(traineeId, userId, username);

        traineeStorage.put(traineeId, trainee);

        dao.delete(traineeId);

        assertNull(traineeStorage.get(traineeId));
    }

    private Trainee buildTrainee(Long traineeId, Long userId, String username) {
        return Trainee.builder()
                .id(traineeId)
                .dateOfBirth(STATIC_BIRTHDAY)
                .address(STATIC_ADDRESS)
                .user(User.builder()
                        .id(userId)
                        .username(username)
                        .firstName(STATIC_FIRSTNAME)
                        .lastName(STATIC_LASTNAME)
                        .password(STATIC_PASSWORD)
                        .isActive(STATIC_IS_ACTIVE)
                        .build())
                .build();
    }
}
