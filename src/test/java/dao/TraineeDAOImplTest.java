package dao;

import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.model.Trainee;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TraineeDAOImplTest {
    private TraineeDAOImpl traineeDAO;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Trainee> traineeStorage;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        storageRegistryMock = Mockito.mock(StorageRegistry.class);
        Mockito.when(storageRegistryMock.getStorage(Mockito.any()))
                .thenReturn((Map) traineeStorage);

        traineeDAO = new TraineeDAOImpl();
        // Directly inject mocked storage (imitate what setStorage would do)
        traineeDAO.setStorage(storageRegistryMock);
    }

    @Test
    void testCreateAndGetById() {
        Trainee trainee = new Trainee(LocalDate.of(2000,1,1), "Some address");
        trainee.setUsername("testuser");
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setPassword("pass");
        trainee.setIsActive(true);

        Trainee created = traineeDAO.create(trainee);

        assertNotNull(created.getId());
        assertEquals(trainee, traineeDAO.getById(created.getId()));
    }

    @Test
    void testUpdate() {
        Trainee trainee = new Trainee(LocalDate.of(2000,1,1), "Some address");
        trainee.setUsername("testuser2");
        trainee.setFirstName("Jane");
        trainee.setLastName("Smith");
        trainee.setPassword("pass2");
        trainee.setIsActive(true);

        Trainee created = traineeDAO.create(trainee);

        created.setAddress("New address");
        Trainee updated = traineeDAO.update(created);

        assertEquals("New address", updated.getAddress());
        assertSame(updated, traineeDAO.getById(updated.getId()));
    }

    @Test
    void testDelete() {
        Trainee trainee = new Trainee(LocalDate.of(2000,1,1), "Some address");
        trainee.setUsername("testuser3");
        trainee.setFirstName("Tom");
        trainee.setLastName("Jerry");
        trainee.setPassword("pass3");
        trainee.setIsActive(true);

        Trainee created = traineeDAO.create(trainee);
        Long id = created.getId();
        assertNotNull(traineeDAO.getById(id));

        traineeDAO.delete(id);

        assertNull(traineeDAO.getById(id));
    }

    @Test
    void testGetByUsername() {
        Trainee trainee = new Trainee(LocalDate.of(2000,1,1), "Addr");
        trainee.setUsername("uniqUser");
        trainee.setFirstName("Name");
        trainee.setLastName("Last");
        trainee.setPassword("ppp");
        trainee.setIsActive(true);

        traineeDAO.create(trainee);

        Trainee found = traineeDAO.getByUsername("uniqUser");
        assertNotNull(found);
        assertEquals("uniqUser", found.getUsername());
    }
}
