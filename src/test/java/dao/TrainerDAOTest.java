package dao;

import com.gca.dao.impl.TrainerDAOImpl;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerDAOTest {

    private TrainerDAOImpl dao;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Trainer> trainerStorage;

    private static final String TRAINER_SPECIALIZATION = "Fitness";
    private static final String TRAINER_USERNAME = "traineruser";
    private static final String TRAINER_FIRSTNAME = "Jane";
    private static final String TRAINER_LASTNAME = "Smith";
    private static final String TRAINER_PASSWORD = "trainerpass";

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        storageRegistryMock = mock(StorageRegistry.class);
        when(storageRegistryMock.getStorage(any()))
                .thenReturn((Map) trainerStorage);

        dao = new TrainerDAOImpl();
        dao.setStorage(storageRegistryMock);
    }

    @Test
    void testCreate() {
        Trainer expected = buildTrainer(1L);

        Trainer actual = dao.create(expected);

        assertNotNull(actual.getUserId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        assertEquals(actual, trainerStorage.get(expected.getUserId()));
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Trainer expected = buildTrainer(id);
        trainerStorage.put(id, expected);

        Trainer actual = dao.getById(id);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
    }

    @Test
    void testGetByUsername() {
        Long id = 2L;
        Trainer expected = buildTrainer(id);
        trainerStorage.put(id, expected);

        Trainer actual = dao.getByUsername(expected.getUsername());

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void testGetAllUsernames() {
        List<String> expected = List.of("trainer1", "trainer2");
        Trainer t1 = buildTrainer(3L, expected.get(0));
        Trainer t2 = buildTrainer(4L, expected.get(1));
        trainerStorage.put(3L, t1);
        trainerStorage.put(4L, t2);

        List<String> actual = dao.getAllUsernames();

        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testUpdate() {
        Long id = 5L;
        Trainer trainer = buildTrainer(id);
        trainerStorage.put(id, trainer);
        Trainer expected = trainer.toBuilder().specialization("Yoga").build();

        Trainer actual = dao.update(expected);

        assertEquals("Yoga", actual.getSpecialization());
        assertEquals(expected, actual);
    }

    private Trainer buildTrainer(Long id, String username) {
        return Trainer.builder()
                .userId(id)
                .username(username)
                .firstName(TRAINER_FIRSTNAME)
                .lastName(TRAINER_LASTNAME)
                .password(TRAINER_PASSWORD)
                .specialization(TRAINER_SPECIALIZATION)
                .isActive(true)
                .build();
    }

    private Trainer buildTrainer(Long id) {
        return Trainer.builder()
                .userId(id)
                .username(TRAINER_USERNAME)
                .firstName(TRAINER_FIRSTNAME)
                .lastName(TRAINER_LASTNAME)
                .password(TRAINER_PASSWORD)
                .specialization(TRAINER_SPECIALIZATION)
                .isActive(true)
                .build();
    }
}
