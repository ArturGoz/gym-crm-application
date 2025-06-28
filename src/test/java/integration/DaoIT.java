package integration;

import com.gca.config.StorageConfig;
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
import com.gca.storage.StorageInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StorageConfig.class,
        StorageInitializer.class,
        TraineeDAOImpl.class,
        TrainerDAOImpl.class,
        TrainingDAOImpl.class
})
class DaoIT {

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
        traineeStorage.clear();
        trainerStorage.clear();
        trainingStorage.clear();

        initializeTestData();
    }

    private void initializeTestData() {
        Trainer trainer1 = Trainer.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("pass123")
                .isActive(true)
                .specialization("Strength")
                .build();
        trainerStorage.put(1L, trainer1);

        Trainer trainer2 = Trainer.builder()
                .userId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .username("jane.smith")
                .password("pass456")
                .isActive(true)
                .specialization("Cardio")
                .build();
        trainerStorage.put(2L, trainer2);

        Trainee trainee1 = Trainee.builder()
                .userId(1L)
                .firstName("Alice")
                .lastName("Johnson")
                .username("alice.johnson")
                .password("pass789")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .build();
        traineeStorage.put(1L, trainee1);

        Trainee trainee2 = Trainee.builder()
                .userId(2L)
                .firstName("Bob")
                .lastName("Williams")
                .username("bob.williams")
                .password("pass012")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1992, 2, 2))
                .address("456 Elm St")
                .build();
        traineeStorage.put(2L, trainee2);

        Training training1 = Training.builder()
                .id(1L)
                .trainerId(1L)
                .traineeId(1L)
                .trainingDate(LocalDate.of(2023, 1, 1))
                .trainingDuration(Duration.parse("PT1H"))
                .trainingName("Beginner Cardio")
                .trainingType(new TrainingType("CARDIO"))
                .build();
        trainingStorage.put(1L, training1);

        Training training2 = Training.builder()
                .id(2L)
                .trainerId(2L)
                .traineeId(2L)
                .trainingDate(LocalDate.of(2023, 1, 2))
                .trainingDuration(Duration.parse("PT2H"))
                .trainingName("Advanced Strength")
                .trainingType(new TrainingType("STRENGTH"))
                .build();
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
        Trainee newTrainee = Trainee.builder()
                .firstName("New")
                .lastName("Trainee")
                .build();

        Trainee createdTrainee = traineeDAO.create(newTrainee);
        assertNotNull(createdTrainee.getUserId());
        assertEquals(3, traineeStorage.size());
        assertTrue(traineeStorage.containsKey(createdTrainee.getUserId()));

        Training newTraining = Training.builder()
                .trainingName("New Training")
                .build();
        Training createdTraining = trainingDAO.create(newTraining);
        assertNotNull(createdTraining.getId());
        assertEquals(3, trainingStorage.size());
        assertTrue(trainingStorage.containsKey(createdTraining.getId()));
    }

    @Test
    void getOperations_ShouldReturnCorrectData() {
        Trainee trainee = traineeDAO.getById(1L);
        assertNotNull(trainee);
        assertEquals("Alice", trainee.getFirstName());
        assertEquals("Johnson", trainee.getLastName());
        assertEquals("123 Main St", trainee.getAddress());
        assertEquals(LocalDate.of(1990, 1, 1), trainee.getDateOfBirth());

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
        Trainee trainee = traineeDAO.getById(1L);
        Trainee updatedTrainee = trainee.toBuilder()
                .address("New Address")
                .build();
        Trainee updated = traineeDAO.update(updatedTrainee);
        assertEquals("New Address", updated.getAddress());
        assertEquals("New Address", traineeStorage.get(1L).getAddress());
    }

    @Test
    void deleteOperations_ShouldRemoveData() {
        traineeDAO.delete(2L);
        assertEquals(1, traineeStorage.size());
        assertNull(traineeStorage.get(2L));
    }
}
