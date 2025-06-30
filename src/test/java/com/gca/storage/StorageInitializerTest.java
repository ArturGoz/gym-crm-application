package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageInitializerTest {

    private StorageInitializer sut;

    @BeforeEach
    void setUp() {
        sut = new StorageInitializer();

        ReflectionTestUtils.setField(sut, "initFilePath", "init-data-test.txt");
    }

    @Test
    @DisplayName("initializeData: should parse file and return correct data")
    void initializeData_successfulParsing() {
        InitializedData result = sut.initializeData();

        assertEquals(1, result.getTrainers().size());

        Trainer trainer = result.getTrainers().iterator().next();

        assertAll("Trainer",
                () -> assertEquals(1L, trainer.getUserId()),
                () -> assertEquals("John", trainer.getFirstName()),
                () -> assertEquals("Doe", trainer.getLastName()),
                () -> assertEquals("john.doe", trainer.getUsername()),
                () -> assertEquals("pass123", trainer.getPassword()),
                () -> assertTrue(trainer.isActive()),
                () -> assertEquals("Strength", trainer.getSpecialization())
        );

        assertEquals(1, result.getTrainees().size());

        Trainee trainee = result.getTrainees().iterator().next();

        assertAll("Trainee",
                () -> assertEquals(1L, trainee.getUserId()),
                () -> assertEquals("Alice", trainee.getFirstName()),
                () -> assertEquals("Johnson", trainee.getLastName()),
                () -> assertEquals("alice.johnson", trainee.getUsername()),
                () -> assertEquals("pass789", trainee.getPassword()),
                () -> assertTrue(trainee.isActive()),
                () -> assertEquals(LocalDate.of(1990, 1, 1),
                        trainee.getDateOfBirth()),
                () -> assertEquals("123 Main St", trainee.getAddress())
        );

        assertEquals(1, result.getTrainings().size());

        Training training = result.getTrainings().iterator().next();

        assertAll("Training",
                () -> assertEquals(1L, training.getId()),
                () -> assertEquals(1L, training.getTrainerId()),
                () -> assertEquals(1L, training.getTraineeId()),
                () -> assertEquals(LocalDate.of(2023, 1, 1),
                        training.getTrainingDate()),
                () -> assertEquals(Duration.parse("PT1H"),
                        training.getTrainingDuration()),
                () -> assertEquals("Beginner Cardio", training.getTrainingName()),
                () -> assertEquals("CARDIO", training.getTrainingType().getName())
        );
    }
}