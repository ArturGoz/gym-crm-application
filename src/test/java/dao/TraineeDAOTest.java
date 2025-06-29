package dao;

import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.model.Trainee;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
    void testCreateAndGetById() {
        Trainee expected = buildTrainee();

        Trainee actual = dao.create(expected);

        assertNotNull(actual.getUserId());
        Trainee actualFromStorage = dao.getById(actual.getUserId());
        assertEquals(actual, actualFromStorage);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.isActive(), actual.isActive());
    }

    @Test
    void testUpdate() {
        Trainee expectedBeforeUpdate = buildTrainee("testuser2", "Jane",
                "Smith", "pass2", "Some address");

        Trainee created = dao.create(expectedBeforeUpdate);

        Trainee updatedTrainee = created.toBuilder()
                .address("New address")
                .build();

        Trainee actual = dao.update(updatedTrainee);

        assertEquals("New address", actual.getAddress());
        assertEquals(updatedTrainee, actual);
        Trainee actualFromStorage = dao.getById(actual.getUserId());
        assertEquals(actual, actualFromStorage);

        assertEquals(created.getUsername(), actual.getUsername());
        assertEquals(created.getFirstName(), actual.getFirstName());
        assertEquals(created.getLastName(), actual.getLastName());
        assertEquals(created.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(created.getPassword(), actual.getPassword());
        assertEquals(created.isActive(), actual.isActive());
    }

    @Test
    void testDelete() {
        Trainee expected = buildTrainee("testuser3", "Tom",
                "Jerry", "pass3", "Some address");

        Trainee actual = dao.create(expected);
        Long id = actual.getUserId();
        assertNotNull(dao.getById(id));

        dao.delete(id);

        Trainee actualAfterDelete = dao.getById(id);
        assertNull(actualAfterDelete);
    }

    @Test
    void testGetByUsername() {
        Trainee expected = buildTrainee("uniqUser", "Name",
                "Last", "ppp", "Addr");

        dao.create(expected);

        Trainee actual = dao.getByUsername("uniqUser");

        assertNotNull(actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.isActive(), actual.isActive());
    }

    private Trainee buildTrainee() {
        return Trainee.builder()
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(TRAINEE_ADDRESS)
                .username(TRAINEE_USERNAME)
                .firstName(TRAINEE_FIRSTNAME)
                .lastName(TRAINEE_LASTNAME)
                .password(TRAINEE_PASSWORD)
                .isActive(true)
                .build();
    }

    private Trainee buildTrainee(String username, String firstName, String lastName, String password, String address) {
        return Trainee.builder()
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(address)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .isActive(true)
                .build();
    }
}
