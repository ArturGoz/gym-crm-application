import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.config.StorageConfig;
import com.gca.storage.StorageInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StorageConfig.class, StorageInitializer.class})
@TestPropertySource(properties = {"storage.init.file=init-data-test.txt"})
public class StorageInitializerIntegrationTest {
    @Autowired
    private Map<Long, Training> trainingStorage;

    @Autowired
    private Map<Long, Trainer> trainerStorage;

    @Autowired
    private Map<Long, Trainee> traineeStorage;

    @Autowired
    private StorageInitializer storageInitializer;

    @Test
    void testInitFilePath() {
        assertEquals("init-data-test.txt", storageInitializer.getInitFilePath(), "initFilePath should be resolved to 'data-test.txt'");
    }

    @Test
    void shouldLoadDataFromFile() {
        assertFalse(trainingStorage.isEmpty(), "Training storage should not be empty");
        assertFalse(trainerStorage.isEmpty(), "Trainer storage should not be empty");
        assertFalse(traineeStorage.isEmpty(), "Trainee storage should not be empty");
    }

    @Test
    void shouldParseTrainingCorrectly() {
        Training training = trainingStorage.get(1L);
        assertNotNull(training, "Training with ID 1 should exist");
        assertEquals(1L, training.getTrainerId(), "Incorrect trainer ID");
        assertEquals(1L, training.getTraineeId(), "Incorrect trainee ID");
        assertEquals("Beginner Cardio", training.getTrainingName(), "Incorrect training name");
        assertEquals("CARDIO", training.getTrainingType().getName(), "Incorrect training type");
        assertEquals(Duration.parse("PT1H"), training.getTrainingDuration(), "Incorrect duration");
    }

    @Test
    void shouldParseTrainerCorrectly() {
        Trainer trainer = trainerStorage.get(1L);
        assertNotNull(trainer, "Trainer with ID 1 should exist");
        assertEquals("John", trainer.getFirstName(), "Incorrect first name");
        assertEquals("Doe", trainer.getLastName(), "Incorrect last name");
        assertEquals("Strength", trainer.getSpecialization(), "Incorrect specialization");
        assertTrue(trainer.getIsActive(), "Trainer should be active");
    }

    @Test
    void shouldParseTraineeCorrectly() {
        Trainee trainee = traineeStorage.get(1L);
        assertNotNull(trainee, "Trainee with ID 1 should exist");
        assertEquals("Alice", trainee.getFirstName(), "Incorrect first name");
        assertEquals("Johnson", trainee.getLastName(), "Incorrect last name");
        assertEquals("123 Main St", trainee.getAddress(), "Incorrect address");
        assertEquals(LocalDate.of(1990, 1, 1), trainee.getDateOfBirth(), "Incorrect date of birth");
        assertTrue(trainee.getIsActive(), "Trainee should be active");
    }
}
