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

class TrainingDAOImplTest {

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
        Training training = new Training();
        training.setTrainerId(1L);
        training.setTraineeId(2L);
        training.setTrainingDate(LocalDate.of(2025, 6, 27));
        training.setTrainingDuration(Duration.ofHours(1));
        training.setTrainingName("Morning Yoga");
        training.setTrainingType(new TrainingType("Yoga"));

        Training created = trainingDAO.create(training);

        assertNotNull(created.getId());
        assertEquals("Morning Yoga", trainingDAO.getById(created.getId()).getTrainingName());
    }

    @Test
    void testGetAll() {
        Training t1 = new Training();
        t1.setTrainerId(1L);
        t1.setTraineeId(2L);
        t1.setTrainingDate(LocalDate.of(2025, 6, 27));
        t1.setTrainingDuration(Duration.ofMinutes(50));
        t1.setTrainingName("Cardio");
        t1.setTrainingType(new TrainingType("Fitness"));

        Training t2 = new Training();
        t2.setTrainerId(3L);
        t2.setTraineeId(4L);
        t2.setTrainingDate(LocalDate.of(2025, 6, 28));
        t2.setTrainingDuration(Duration.ofMinutes(45));
        t2.setTrainingName("Boxing");
        t2.setTrainingType(new TrainingType("Martial Arts"));

        trainingDAO.create(t1);
        trainingDAO.create(t2);

        assertEquals(2, trainingDAO.getAll().size());
    }

    @Test
    void testCreateAssignsUniqueIds() {
        Training t1 = new Training();
        t1.setTrainingName("T1");
        Training t2 = new Training();
        t2.setTrainingName("T2");

        Training created1 = trainingDAO.create(t1);
        Training created2 = trainingDAO.create(t2);

        assertNotEquals(created1.getId(), created2.getId());
    }

    @Test
    void testGetByIdReturnsNullIfNotFound() {
        assertNull(trainingDAO.getById(12345L));
    }
}
