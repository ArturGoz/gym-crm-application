package dao;

import com.gca.dao.impl.TrainingDAOImpl;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.storage.StorageRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrainingDAOTest {

    private TrainingDAOImpl trainingDAO;
    private StorageRegistry storageRegistryMock;
    private Map<Long, Training> trainingStorage;

    @BeforeEach
    void setUp() {
        trainingStorage = new HashMap<>();
        storageRegistryMock = Mockito.mock(StorageRegistry.class);
        Mockito.when(storageRegistryMock.getStorage(Mockito.any()))
                .thenReturn((Map) trainingStorage);

        trainingDAO = new TrainingDAOImpl();
        trainingDAO.setStorage(storageRegistryMock);
    }

    @Test
    void testCreateAndGetById() {
        Training training = Training.builder()
                .trainerId(1L)
                .traineeId(2L)
                .trainingDate(LocalDate.of(2025, 6, 27))
                .trainingDuration(Duration.ofHours(1))
                .trainingName("Morning Yoga")
                .trainingType(new TrainingType("Yoga"))
                .build();

        Training created = trainingDAO.create(training);

        assertNotNull(created.getId());
        assertEquals("Morning Yoga", trainingDAO.getById(created.getId()).getTrainingName());
    }

    @Test
    void testCreateAssignsUniqueIds() {
        Training t1 = Training.builder()
                .trainingName("T1")
                .build();
        Training t2 = Training.builder()
                .trainingName("T2")
                .build();

        Training created1 = trainingDAO.create(t1);
        Training created2 = trainingDAO.create(t2);

        assertNotEquals(created1.getId(), created2.getId());
    }

    @Test
    void testGetByIdReturnsNullIfNotFound() {
        assertNull(trainingDAO.getById(12345L));
    }
}
