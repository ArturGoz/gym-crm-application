package com.gca.dao;

import com.gca.dao.impl.TrainerDAOImpl;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainerDAOTest {

    private static final Long USER_ID = 1001L;
    private static final Long TYPE_ID = 991L;

    private static final String USERNAME = "traineruser";
    private static final String FIRSTNAME = "Jane";
    private static final String LASTNAME = "Smith";
    private static final String PASSWORD = "trainerpass";
    private static final String TYPE_NAME = "Fitness";

    private static final boolean IS_ACTIVE = true;

    private TrainerDAOImpl dao;

    private Map<Long, Trainer> trainerStorage;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        //dao = new TrainerDAOImpl();

        ReflectionTestUtils.setField(dao, "storage", trainerStorage);
    }

    @Test
    void shouldSuccessfullyCreateTrainer() {
        Trainer expected = buildTrainer(1L, USER_ID, USERNAME);

        Trainer actual = dao.create(expected);

        assertNotNull(actual.getId());
        assertEquals(expected.getId(), actual.getId());

        assertNotNull(actual.getUser());
        assertEquals(expected.getUser().getId(), actual.getUser().getId());
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
        assertEquals(expected.getUser().getLastName(), actual.getUser().getLastName());
        assertEquals(expected.getUser().getPassword(), actual.getUser().getPassword());
        assertEquals(expected.getUser().getIsActive(), actual.getUser().getIsActive());

        assertNotNull(actual.getSpecialization());
        assertEquals(expected.getSpecialization().getId(), actual.getSpecialization().getId());
        assertEquals(expected.getSpecialization().getName(), actual.getSpecialization().getName());

        assertEquals(actual, trainerStorage.get(expected.getId()));
    }

    @Test
    void shouldReturnTrainerById() {
        Long trainerId = 10L;
        Long userId = 20L;
        String username = "user10";
        Trainer expected = buildTrainer(trainerId, userId, username);

        trainerStorage.put(trainerId, expected);

        Trainer actual = dao.getById(trainerId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
    }

    @Test
    void shouldUpdateTrainer() {
        Long trainerId = 5L;
        Long userId = 105L;
        String username = "upduser";
        Trainer trainer = buildTrainer(trainerId, userId, username);

        trainerStorage.put(trainerId, trainer);

        TrainingType newType = TrainingType.builder()
                .id(123L)
                .name("Yoga")
                .build();

        Trainer expected = trainer.toBuilder()
                .specialization(newType)
                .build();

        Trainer actual = dao.update(expected);

        assertEquals("Yoga", actual.getSpecialization().getName());
        assertEquals(expected, actual);
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
