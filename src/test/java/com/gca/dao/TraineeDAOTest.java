package com.gca.dao;

import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.model.Trainee;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraineeDAOTest {

    private TraineeDAOImpl dao;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Trainee> traineeStorage;

    private static final String TRAINEE_ADDRESS = "Some address";
    private static final String TRAINEE_USERNAME = "testuser";
    private static final String TRAINEE_FIRSTNAME = "John";
    private static final String TRAINEE_LASTNAME = "Doe";
    private static final String TRAINEE_PASSWORD = "pass";
    private static final LocalDate TRAINEE_BIRTHDAY = LocalDate.of(2000, 1, 1);

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        storageRegistryMock = mock(StorageRegistry.class);
        when(storageRegistryMock.getStorage(any()))
                .thenReturn((Map) traineeStorage);

        dao = new TraineeDAOImpl();
        dao.setStorage(storageRegistryMock);
    }

    @Test
    void testCreate() {
        Trainee expected = buildTrainee(1L);

        Trainee actual = dao.create(expected);

        assertNotNull(actual.getUserId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(actual, traineeStorage.get(expected.getUserId()));
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Trainee expected = buildTrainee(id);
        traineeStorage.put(id, expected);

        Trainee actual = dao.getById(id);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
    }

    @Test
    void testGetByUsername() {
        Long id = 2L;
        Trainee expected = buildTrainee(id);
        traineeStorage.put(id, expected);

        Trainee actual = dao.getByUsername(expected.getUsername());

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void testGetAllUsernames() {
        List<String> expected = List.of("user1", "user2");
        Trainee t1 = buildTrainee(3L, expected.get(0));
        Trainee t2 = buildTrainee(4L, expected.get(1));
        traineeStorage.put(3L, t1);
        traineeStorage.put(4L, t2);

        List<String> actual = dao.getAllUsernames();

        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testUpdate() {
        Long id = 5L;
        Trainee trainee = buildTrainee(id);
        traineeStorage.put(id, trainee);
        Trainee expected = trainee.toBuilder().address("new address").build();

        Trainee actual = dao.update(expected);

        assertEquals("new address", actual.getAddress());
        assertEquals(expected, actual);
    }

    @Test
    void testDelete() {
        Long id = 6L;
        Trainee trainee = buildTrainee(id);
        traineeStorage.put(id, trainee);

        dao.delete(id);

        assertNull(traineeStorage.get(id));
    }

    private Trainee buildTrainee(Long id, String username) {
        return Trainee.builder()
                .userId(id)
                .username(username)
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(TRAINEE_ADDRESS)
                .firstName(TRAINEE_FIRSTNAME)
                .lastName(TRAINEE_LASTNAME)
                .password(TRAINEE_PASSWORD)
                .isActive(true)
                .build();
    }

    private Trainee buildTrainee(Long id) {
        return Trainee.builder()
                .userId(id)
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(TRAINEE_ADDRESS)
                .username(TRAINEE_USERNAME)
                .firstName(TRAINEE_FIRSTNAME)
                .lastName(TRAINEE_LASTNAME)
                .password(TRAINEE_PASSWORD)
                .isActive(true)
                .build();
    }
}
