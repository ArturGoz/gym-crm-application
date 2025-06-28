package dao;

import com.gca.dao.impl.TrainerDAOImpl;
import com.gca.model.Trainer;
import com.gca.storage.Namespace;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDAOImplTest {

    private TrainerDAOImpl trainerDAO;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Trainer> trainerStorage;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        storageRegistryMock = Mockito.mock(StorageRegistry.class);
        Mockito.when(storageRegistryMock.getStorage(Mockito.any()))
                .thenReturn((Map) trainerStorage);

        trainerDAO = new TrainerDAOImpl();
        trainerDAO.setStorage(storageRegistryMock);
    }

    @Test
    void testCreateAndGetById() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer1");
        trainer.setFirstName("Alice");
        trainer.setLastName("Smith");
        trainer.setPassword("pass");
        trainer.setIsActive(true);
        trainer.setSpecialization("Yoga");

        Trainer created = trainerDAO.create(trainer);

        assertNotNull(created.getId());
        assertEquals(trainer, trainerDAO.getById(created.getId()));
    }

    @Test
    void testUpdate() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer2");
        trainer.setFirstName("Bob");
        trainer.setLastName("Jones");
        trainer.setPassword("pass2");
        trainer.setIsActive(true);
        trainer.setSpecialization("Boxing");

        Trainer created = trainerDAO.create(trainer);

        created.setSpecialization("CrossFit");
        Trainer updated = trainerDAO.update(created);

        assertEquals("CrossFit", updated.getSpecialization());
        assertSame(updated, trainerDAO.getById(updated.getId()));
    }

    @Test
    void testGetByUsername() {
        Trainer trainer = new Trainer();
        trainer.setUsername("uniqueTrainer");
        trainer.setFirstName("Carla");
        trainer.setLastName("White");
        trainer.setPassword("pw");
        trainer.setIsActive(true);
        trainer.setSpecialization("Pilates");

        trainerDAO.create(trainer);

        Trainer found = trainerDAO.getByUsername("uniqueTrainer");
        assertNotNull(found);
        assertEquals("uniqueTrainer", found.getUsername());
    }

    @Test
    void testGetAll() {
        Trainer trainer1 = new Trainer();
        trainer1.setUsername("t1");
        trainer1.setFirstName("A");
        trainer1.setLastName("A");
        trainer1.setPassword("1");
        trainer1.setIsActive(true);
        trainer1.setSpecialization("Spinning");

        Trainer trainer2 = new Trainer();
        trainer2.setUsername("t2");
        trainer2.setFirstName("B");
        trainer2.setLastName("B");
        trainer2.setPassword("2");
        trainer2.setIsActive(true);
        trainer2.setSpecialization("Cardio");

        trainerDAO.create(trainer1);
        trainerDAO.create(trainer2);

        assertEquals(2, trainerDAO.getAll().size());
    }

    @Test
    void testUpdateNonExistentTrainerThrows() {
        Trainer trainer = new Trainer();
        trainer.setId(999L);
        trainer.setUsername("noSuchTrainer");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> trainerDAO.update(trainer));
        assertTrue(ex.getMessage().contains("not found with id: 999"));
    }
}
