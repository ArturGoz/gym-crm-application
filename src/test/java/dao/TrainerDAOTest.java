package dao;

import com.gca.dao.impl.TrainerDAOImpl;
import com.gca.model.Trainer;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerDAOTest {

    private TrainerDAOImpl dao;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Trainer> trainerStorage;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        storageRegistryMock = Mockito.mock(StorageRegistry.class);
        Mockito.when(storageRegistryMock.getStorage(Mockito.any()))
                .thenReturn((Map) trainerStorage);

        dao = new TrainerDAOImpl();
        dao.setStorage(storageRegistryMock);
    }

    @Test
    void testCreateAndGetById() {
        Trainer trainer = Trainer.builder()
                .username("trainer1")
                .firstName("Alice")
                .lastName("Smith")
                .password("pass")
                .isActive(true)
                .specialization("Yoga")
                .build();

        Trainer created = dao.create(trainer);

        assertNotNull(created.getUserId());
        assertEquals(created, dao.getById(created.getUserId()));
    }

    @Test
    void testUpdate() {
        Trainer trainer = Trainer.builder()
                .username("trainer2")
                .firstName("Bob")
                .lastName("Jones")
                .password("pass2")
                .isActive(true)
                .specialization("Boxing")
                .build();

        Trainer created = dao.create(trainer);

        Trainer updatedTrainer = created.toBuilder()
                .specialization("CrossFit")
                .build();
        Trainer updated = dao.update(updatedTrainer);

        assertEquals("CrossFit", updated.getSpecialization());
        assertSame(updated, dao.getById(updated.getUserId()));
    }

    @Test
    void testGetByUsername() {
        Trainer trainer = Trainer.builder()
                .username("uniqueTrainer")
                .firstName("Carla")
                .lastName("White")
                .password("pw")
                .isActive(true)
                .specialization("Pilates")
                .build();

        dao.create(trainer);

        Trainer found = dao.getByUsername("uniqueTrainer");
        assertNotNull(found);
        assertEquals("uniqueTrainer", found.getUsername());
    }

    @Test
    void testGetAllUsernames() {
        Trainer trainer1 = Trainer.builder()
                .username("t1")
                .firstName("A")
                .lastName("A")
                .password("1")
                .isActive(true)
                .specialization("Spinning")
                .build();

        Trainer trainer2 = Trainer.builder()
                .username("t2")
                .firstName("B")
                .lastName("B")
                .password("2")
                .isActive(true)
                .specialization("Cardio")
                .build();

        dao.create(trainer1);
        dao.create(trainer2);

        assertEquals(2, dao.getAllUsernames().size());
    }

    @Test
    void testUpdateNonExistentTrainerThrows() {
        Trainer trainer = Trainer.builder()
                .userId(999L)
                .username("noSuchTrainer")
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.update(trainer));
        assertTrue(ex.getMessage().contains("not found with id: 999"));
    }
}
