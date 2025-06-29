package dao;

import com.gca.dao.impl.TrainerDAOImpl;
import com.gca.model.Trainer;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerDAOTest {

    private TrainerDAOImpl dao;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Trainer> trainerStorage;

    private static final String TRAINER_USERNAME = "trainer1";
    private static final String TRAINER_FIRSTNAME = "Alice";
    private static final String TRAINER_LASTNAME = "Smith";
    private static final String TRAINER_PASSWORD = "pass";
    private static final String TRAINER_SPECIALIZATION = "Yoga";
    private static final boolean TRAINER_IS_ACTIVE = true;

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
    void testCreateAndGetById() {
        Trainer expected = buildTrainer();

        Trainer actual = dao.create(expected);

        assertNotNull(actual.getUserId());
        Trainer actualFromStorage = dao.getById(actual.getUserId());
        assertEquals(actual, actualFromStorage);

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
    }

    @Test
    void testUpdate() {
        Trainer expectedBeforeUpdate = buildTrainer("trainer2", "Bob",
                "Jones", "pass2", "Boxing");

        Trainer created = dao.create(expectedBeforeUpdate);

        Trainer updatedTrainer = created.toBuilder()
                .specialization("CrossFit")
                .build();

        Trainer actual = dao.update(updatedTrainer);

        assertEquals("CrossFit", actual.getSpecialization());
        assertEquals(updatedTrainer, actual);
        Trainer actualFromStorage = dao.getById(actual.getUserId());
        assertEquals(actual, actualFromStorage);

        assertEquals(created.getUsername(), actual.getUsername());
        assertEquals(created.getFirstName(), actual.getFirstName());
        assertEquals(created.getLastName(), actual.getLastName());
        assertEquals(created.getPassword(), actual.getPassword());
        assertEquals(created.isActive(), actual.isActive());
    }

    @Test
    void testGetByUsername() {
        Trainer expected = buildTrainer("uniqueTrainer", "Carla",
                "White", "pw", "Pilates");

        dao.create(expected);

        Trainer actual = dao.getByUsername("uniqueTrainer");

        assertNotNull(actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
    }

    @Test
    void testGetAllUsernames() {
        Trainer trainer1 = buildTrainer("t1", "A",
                "A", "1", "Spinning");
        Trainer trainer2 = buildTrainer("t2", "B",
                "B", "2", "Cardio");

        dao.create(trainer1);
        dao.create(trainer2);

        List<String> actualUsernames = dao.getAllUsernames();

        assertEquals(2, actualUsernames.size());
        assertTrue(actualUsernames.contains("t1"));
        assertTrue(actualUsernames.contains("t2"));
    }

    @Test
    void testUpdateNonExistentTrainerThrows() {
        Trainer nonExistentTrainer = Trainer.builder()
                .userId(999L)
                .username("noSuchTrainer")
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.update(nonExistentTrainer));
        assertTrue(ex.getMessage().contains("not found with id: 999"));
    }

    private Trainer buildTrainer() {
        return Trainer.builder()
                .username(TRAINER_USERNAME)
                .firstName(TRAINER_FIRSTNAME)
                .lastName(TRAINER_LASTNAME)
                .password(TRAINER_PASSWORD)
                .isActive(TRAINER_IS_ACTIVE)
                .specialization(TRAINER_SPECIALIZATION)
                .build();
    }

    private Trainer buildTrainer(String username, String firstName, String lastName, String password, String specialization) {
        return Trainer.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .isActive(true)
                .specialization(specialization)
                .build();
    }
}
