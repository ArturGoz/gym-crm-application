import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingDAO;
import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.dao.impl.TrainerDAOImpl;
import com.gca.dao.impl.TrainingDAOImpl;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.storage.StorageConfig;
import com.gca.storage.StorageInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StorageConfig.class,
        StorageInitializer.class,
        TraineeDAOImpl.class,
        TrainerDAOImpl.class,
        TrainingDAOImpl.class
})
class DaoIntegrationTest {

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private TrainingDAO trainingDAO;

    @Autowired
    private Map<Long, Trainee> traineeStorage;

    @Autowired
    private Map<Long, Trainer> trainerStorage;

    @Autowired
    private Map<Long, Training> trainingStorage;

    @BeforeEach
    void resetState() {
        // Очищаємо сховища перед кожним тестом
        traineeStorage.clear();
        trainerStorage.clear();
        trainingStorage.clear();

        // Переініціалізуємо початкові дані
        initializeTestData();
    }

    private void initializeTestData() {
        // Ініціалізація тренерів
        Trainer trainer1 = new Trainer();
        trainer1.setUserId(1L);
        trainer1.setFirstName("John");
        trainer1.setLastName("Doe");
        trainer1.setUsername("john.doe");
        trainer1.setPassword("pass123");
        trainer1.setActive(true);
        trainer1.setSpecialization("Strength");
        trainerStorage.put(1L, trainer1);

        Trainer trainer2 = new Trainer();
        trainer2.setUserId(2L);
        trainer2.setFirstName("Jane");
        trainer2.setLastName("Smith");
        trainer2.setUsername("jane.smith");
        trainer2.setPassword("pass456");
        trainer2.setActive(true);
        trainer2.setSpecialization("Cardio");
        trainerStorage.put(2L, trainer2);

        // Ініціалізація стажерів
        Trainee trainee1 = new Trainee();
        trainee1.setUserId(1L);
        trainee1.setFirstName("Alice");
        trainee1.setLastName("Johnson");
        trainee1.setUsername("alice.johnson");
        trainee1.setPassword("pass789");
        trainee1.setActive(true);
        trainee1.setDateOfBirth(Date.valueOf("1990-01-01"));
        trainee1.setAddress("123 Main St");
        traineeStorage.put(1L, trainee1);

        Trainee trainee2 = new Trainee();
        trainee2.setUserId(2L);
        trainee2.setFirstName("Bob");
        trainee2.setLastName("Williams");
        trainee2.setUsername("bob.williams");
        trainee2.setPassword("pass012");
        trainee2.setActive(true);
        trainee2.setDateOfBirth(Date.valueOf("1992-02-02"));
        trainee2.setAddress("456 Elm St");
        traineeStorage.put(2L, trainee2);

        // Ініціалізація тренувань
        Training training1 = new Training();
        training1.setId(1L);
        training1.setTrainerId(1L);
        training1.setTraineeId(1L);
        training1.setTrainingDate(Date.valueOf("2023-01-01"));
        training1.setTrainingDuration(Duration.parse("PT1H"));
        training1.setTrainingName("Beginner Cardio");
        training1.setTrainingType(new TrainingType("CARDIO"));
        trainingStorage.put(1L, training1);

        Training training2 = new Training();
        training2.setId(2L);
        training2.setTrainerId(2L);
        training2.setTraineeId(2L);
        training2.setTrainingDate(Date.valueOf("2023-01-02"));
        training2.setTrainingDuration(Duration.parse("PT2H"));
        training2.setTrainingName("Advanced Strength");
        training2.setTrainingType(new TrainingType("STRENGTH"));
        trainingStorage.put(2L, training2);
    }

    @Test
    void contextLoads() {
        assertNotNull(traineeDAO);
        assertNotNull(trainerDAO);
        assertNotNull(trainingDAO);
        assertNotNull(traineeStorage);
        assertNotNull(trainerStorage);
        assertNotNull(trainingStorage);
    }

    @Test
    void storageInitialization_ShouldHaveCorrectSize() {
        assertEquals(2, traineeStorage.size());
        assertEquals(2, trainerStorage.size());
        assertEquals(2, trainingStorage.size());
    }

    @Test
    void createOperations_ShouldUpdateStorage() {
        // Перевіряємо операції створення
        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName("New");
        newTrainee.setLastName("Trainee");

        Trainee createdTrainee = traineeDAO.create(newTrainee);
        assertNotNull(createdTrainee.getUserId());
        assertEquals(3, traineeStorage.size());
        assertTrue(traineeStorage.containsKey(createdTrainee.getUserId()));

        Training newTraining = new Training();
        newTraining.setTrainingName("New Training");
        Training createdTraining = trainingDAO.create(newTraining);
        assertNotNull(createdTraining.getId());
        assertEquals(3, trainingStorage.size());
        assertTrue(trainingStorage.containsKey(createdTraining.getId()));
    }

    @Test
    void getOperations_ShouldReturnCorrectData() {
        // Перевіряємо, що дані з ініціалізації доступні через DAO
        Trainee trainee = traineeDAO.getById(1L);
        assertNotNull(trainee);
        assertEquals("Alice", trainee.getFirstName());
        assertEquals("Johnson", trainee.getLastName());
        assertEquals("123 Main St", trainee.getAddress());
        assertEquals(Date.valueOf("1990-01-01"), trainee.getDateOfBirth());

        Trainer trainer = trainerDAO.getById(1L);
        assertNotNull(trainer);
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
        assertEquals("Strength", trainer.getSpecialization());

        Training training = trainingDAO.getById(1L);
        assertNotNull(training);
        assertEquals("Beginner Cardio", training.getTrainingName());
        assertEquals(Duration.parse("PT1H"), training.getTrainingDuration());
        assertEquals("CARDIO", training.getTrainingType().getName());
    }

    @Test
    void updateOperations_ShouldModifyData() {
        // Перевіряємо операції оновлення
        Trainee trainee = traineeDAO.getById(1L);
        trainee.setAddress("New Address");
        Trainee updated = traineeDAO.update(trainee);
        assertEquals("New Address", updated.getAddress());
        assertEquals("New Address", traineeStorage.get(1L).getAddress());
    }

    @Test
    void deleteOperations_ShouldRemoveData() {
        // Перевіряємо операції видалення
        traineeDAO.delete(2L);
        assertEquals(1, traineeStorage.size());
        assertNull(traineeStorage.get(2L));
    }
}
