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

class TraineeDAOTest {
    private TraineeDAOImpl dao;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Trainee> traineeStorage;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        storageRegistryMock = Mockito.mock(StorageRegistry.class);
        Mockito.when(storageRegistryMock.getStorage(Mockito.any()))
                .thenReturn((Map) traineeStorage);

        dao = new TraineeDAOImpl();
        dao.setStorage(storageRegistryMock);
    }

    @Test
    void testCreateAndGetById() {
        Trainee trainee = Trainee.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Some address")
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .password("pass")
                .isActive(true)
                .build();

        Trainee created = dao.create(trainee);

        assertNotNull(created.getUserId());
        assertEquals(created, dao.getById(created.getUserId()));
    }

    @Test
    void testUpdate() {
        Trainee trainee = Trainee.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Some address")
                .username("testuser2")
                .firstName("Jane")
                .lastName("Smith")
                .password("pass2")
                .isActive(true)
                .build();

        Trainee created = dao.create(trainee);

        Trainee updated = created.toBuilder()
                .address("New address")
                .build();
        updated = dao.update(updated);

        assertEquals("New address", updated.getAddress());
        assertSame(updated, dao.getById(updated.getUserId()));
    }

    @Test
    void testDelete() {
        Trainee trainee = Trainee.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Some address")
                .username("testuser3")
                .firstName("Tom")
                .lastName("Jerry")
                .password("pass3")
                .isActive(true)
                .build();

        Trainee created = dao.create(trainee);
        Long id = created.getUserId();
        assertNotNull(dao.getById(id));

        dao.delete(id);

        assertNull(dao.getById(id));
    }

    @Test
    void testGetByUsername() {
        Trainee trainee = Trainee.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Addr")
                .username("uniqUser")
                .firstName("Name")
                .lastName("Last")
                .password("ppp")
                .isActive(true)
                .build();

        dao.create(trainee);

        Trainee found = dao.getByUsername("uniqUser");
        assertNotNull(found);
        assertEquals("uniqUser", found.getUsername());
    }
}
