package service;

import com.gca.dao.TrainingDAO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    @Mock
    private TrainingDAO dao;
    @Mock
    private TrainingMapper mapper;

    @InjectMocks
    private TrainingServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_success() {
        TrainingType trainingType = TrainingType.builder()
                .name("Cardio")
                .build();

        TrainingCreateRequest request = TrainingCreateRequest.builder()
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(trainingType)
                .build();

        Training training = Training.builder()
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(trainingType)
                .build();

        when(mapper.toEntity(request)).thenReturn(training);

        Training created = training.toBuilder()
                .id(1L)
                .build();

        when(dao.create(any(Training.class))).thenReturn(created);

        TrainingResponse expectedResponse = TrainingResponse.builder()
                .id(1L)
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(trainingType)
                .build();

        when(mapper.toResponse(any(Training.class))).thenReturn(expectedResponse);

        TrainingResponse response = service.createTraining(request);

        assertEquals(expectedResponse, response);
        verify(mapper).toEntity(request);
        verify(dao).create(any(Training.class));
        verify(mapper).toResponse(any(Training.class));
    }

    @Test
    void getTrainingById_success() {
        TrainingType trainingType = TrainingType.builder()
                .name("Strength")
                .build();

        Training training = Training.builder()
                .id(2L)
                .trainerId(11L)
                .traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(45))
                .trainingName("Evening Strength")
                .trainingType(trainingType)
                .build();

        TrainingResponse response = TrainingResponse.builder()
                .id(2L)
                .trainerId(11L)
                .traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(45))
                .trainingName("Evening Strength")
                .trainingType(trainingType)
                .build();

        when(dao.getById(2L)).thenReturn(training);
        when(mapper.toResponse(training)).thenReturn(response);

        TrainingResponse result = service.getTrainingById(2L);

        assertEquals(response, result);
        verify(dao).getById(2L);
        verify(mapper).toResponse(training);
    }
}
