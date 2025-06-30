package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import java.time.LocalDate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StorageInitializerTest {

    private static final String TRAINER_DATA = "[Trainer]\n1,John,Doe,john.doe,pass123,true,Strength";
    private static final String TRAINEE_DATA = "[Trainee]\n1,Alice,Johnson,alice.johnson,pass789,true,1990-01-01,123 Main St";
    private static final String TRAINING_DATA = "[Training]\n1,1,1,2023-01-01,PT1H,Beginner Cardio,CARDIO";
    private static final String TEST_DATA = buildStorageInitData();

    private StorageInitializer sut;

    private Supplier<Reader> readerSupplier;

    @BeforeEach
    void setUp() {
        readerSupplier = mock(Supplier.class);
        sut = new StorageInitializer();
        sut.setReaderSupplier(readerSupplier);
        sut.setInitFilePath("mocked-file.txt");
    }

    @Test
    @DisplayName("initializeData: should parse file and return correct data")
    void initializeData_successfulParsing() throws Exception {
        when(readerSupplier.get()).thenReturn(new StringReader(TEST_DATA));

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
                () -> assertEquals(LocalDate.of(1990, 1, 1), trainee.getDateOfBirth()),
                () -> assertEquals("123 Main St", trainee.getAddress())
        );

        assertEquals(1, result.getTrainings().size());
        Training training = result.getTrainings().iterator().next();
        assertAll("Training",
                () -> assertEquals(1L, training.getId()),
                () -> assertEquals(1L, training.getTrainerId()),
                () -> assertEquals(1L, training.getTraineeId()),
                () -> assertEquals(LocalDate.of(2023, 1, 1), training.getTrainingDate()),
                () -> assertEquals(Duration.parse("PT1H"), training.getTrainingDuration()),
                () -> assertEquals("Beginner Cardio", training.getTrainingName()),
                () -> assertEquals("CARDIO", training.getTrainingType().getName())
        );

        verify(readerSupplier, times(1)).get();
    }

    @Test
    @DisplayName("initializeData: should wrap IOException in RuntimeException")
    void initializeData_ioException() throws Exception {
        Reader mockedReader = mock(Reader.class);
        doThrow(new IOException("Mocked IO error"))
                .when(mockedReader)
                .read(any(char[].class), anyInt(), anyInt());
        when(readerSupplier.get()).thenReturn(mockedReader);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.initializeData());
        assertTrue(ex.getMessage().contains("Failed to initialize data from file: mocked-file.txt"));
        assertInstanceOf(IOException.class, ex.getCause());

        verify(readerSupplier, times(1)).get();
        verify(mockedReader).read(any(char[].class), anyInt(), anyInt());
    }

    private static String buildStorageInitData() {
        return String.join("\n", TRAINER_DATA, TRAINEE_DATA, TRAINING_DATA);
    }
}
