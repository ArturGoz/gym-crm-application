import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.storage.StorageConfig;
import com.gca.storage.StorageInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StorageConfig.class, StorageInitializer.class})
public class StorageInitializerTest {
    @Autowired
    private Map<Long, Training> trainingStorage;

    @Autowired
    private Map<Long, Trainer> trainerStorage;

    @Autowired
    private Map<Long, Trainee> traineeStorage;


    @Test
    void testTrainingStorageInitialization() {
        assertFalse(trainingStorage.isEmpty(), "Training storage should not be empty");
        assertEquals(2, trainingStorage.size(), "Training storage should contain 2 entries");

        Training training = trainingStorage.get(1L);
        assertNotNull(training, "Training with ID 1 should exist");
        assertEquals(1L, training.getTrainerId(), "Trainer ID should be 1");
        assertEquals(1L, training.getTraineeId(), "Trainee ID should be 1");
        assertEquals("Beginner Cardio", training.getTrainingName(), "Training name should be Beginner Cardio");
        assertEquals("CARDIO", training.getTrainingType().getName(), "Training type should be CARDIO");
    }

    @Test
    void testTrainerStorageInitialization() {
        assertFalse(trainerStorage.isEmpty(), "Trainer storage should not be empty");
        assertEquals(2, trainerStorage.size(), "Trainer storage should contain 2 entries");

        Trainer trainer = trainerStorage.get(1L);
        assertNotNull(trainer, "Trainer with ID 1 should exist");
        assertEquals("John", trainer.getFirstName(), "First name should be John");
        assertEquals("Doe", trainer.getLastName(), "Last name should be Doe");
        assertEquals("john.doe", trainer.getUsername(), "Username should be john.doe");
        assertEquals("Strength", trainer.getSpecialization(), "Specialization should be Strength");
    }

    @Test
    void testTraineeStorageInitialization() {
        assertFalse(traineeStorage.isEmpty(), "Trainee storage should not be empty");
        assertEquals(2, traineeStorage.size(), "Trainee storage should contain 2 entries");

        Trainee trainee = traineeStorage.get(1L);
        assertNotNull(trainee, "Trainee with ID 1 should exist");
        assertEquals("Alice", trainee.getFirstName(), "First name should be Alice");
        assertEquals("Johnson", trainee.getLastName(), "Last name should be Johnson");
        assertEquals("alice.johnson", trainee.getUsername(), "Username should be alice.johnson");
        assertEquals("123 Main St", trainee.getAddress(), "Address should be 123 Main St");
    }

    @Test
    void testSameStorageInstance() {
        StorageInitializer initializer = new StorageInitializer();
        initializer.setTrainingStorage(trainingStorage);
        initializer.setTrainerStorage(trainerStorage);
        initializer.setTraineeStorage(traineeStorage);

        assertSame(trainingStorage, initializer.getTrainingStorage(), "Training storage instances should be the same");
        assertSame(trainerStorage, initializer.getTrainerStorage(), "Trainer storage instances should be the same");
        assertSame(traineeStorage, initializer.getTraineeStorage(), "Trainee storage instances should be the same");
    }

}
