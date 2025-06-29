package storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.storage.InitializedData;
import com.gca.storage.StorageInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import java.time.LocalDate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StorageInitializerTest {

    private StorageInitializer storageInitializer;
    private Supplier<Reader> readerSupplier;

    @BeforeEach
    public void setUp() {
        storageInitializer = new StorageInitializer();
        readerSupplier = mock(Supplier.class);

        storageInitializer.setReaderSupplier(readerSupplier);
        storageInitializer.setInitFilePath("mocked-file.txt");
    }

    @Test
    public void testInitializeData_successfulParsing() throws Exception {
        String testData = """
                [Trainer]
                1,John,Doe,john.doe,pass123,true,Strength
                [Trainee]
                1,Alice,Johnson,alice.johnson,pass789,true,1990-01-01,123 Main St
                [Training]
                1,1,1,2023-01-01,PT1H,Beginner Cardio,CARDIO
                """;

        Reader stringReader = new StringReader(testData);
        when(readerSupplier.get()).thenReturn(stringReader);

        InitializedData data = storageInitializer.initializeData();

        assertEquals(1, data.getTrainers().size());
        Trainer trainer = data.getTrainers().iterator().next();
        assertEquals(1L, trainer.getUserId());
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
        assertEquals("john.doe", trainer.getUsername());
        assertEquals("pass123", trainer.getPassword());
        assertTrue(trainer.isActive());
        assertEquals("Strength", trainer.getSpecialization());

        assertEquals(1, data.getTrainees().size());
        Trainee trainee = data.getTrainees().iterator().next();
        assertEquals(1L, trainee.getUserId());
        assertEquals("Alice", trainee.getFirstName());
        assertEquals("Johnson", trainee.getLastName());
        assertEquals("alice.johnson", trainee.getUsername());
        assertEquals("pass789", trainee.getPassword());
        assertTrue(trainee.isActive());
        assertEquals(LocalDate.of(1990, 1, 1), trainee.getDateOfBirth());
        assertEquals("123 Main St", trainee.getAddress());

        assertEquals(1, data.getTrainings().size());
        Training training = data.getTrainings().iterator().next();
        assertEquals(1L, training.getId());
        assertEquals(1L, training.getTrainerId());
        assertEquals(1L, training.getTraineeId());
        assertEquals(LocalDate.of(2023, 1, 1), training.getTrainingDate());
        assertEquals(Duration.parse("PT1H"), training.getTrainingDuration());
        assertEquals("Beginner Cardio", training.getTrainingName());
        assertEquals("CARDIO", training.getTrainingType().getName());

        verify(readerSupplier, times(1)).get();
    }

    @Test
    public void testInitializeData_ioException() throws Exception {
        Reader mockedReader = mock(Reader.class);
        doThrow(new IOException("Mocked IO error"))
                .when(mockedReader)
                .read(any(char[].class), anyInt(), anyInt());

        when(readerSupplier.get()).thenReturn(mockedReader);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            storageInitializer.initializeData();
        });

        assertTrue(exception.getMessage().contains("Failed to initialize data from file: mocked-file.txt"));
        assertTrue(exception.getCause() instanceof IOException);

        verify(readerSupplier, times(1)).get();
        verify(mockedReader).read(any(char[].class), anyInt(), anyInt());
    }
}
