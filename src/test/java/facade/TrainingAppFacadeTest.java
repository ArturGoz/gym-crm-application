package facade;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.facade.TrainingAppFacade;
import com.gca.model.TrainingType;
import com.gca.service.TraineeService;
import com.gca.service.TrainerService;
import com.gca.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingAppFacadeTest {

    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingAppFacade facade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_delegatesToService() {
        TraineeCreateRequest request = TraineeCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Kyiv")
                .build();
        TraineeResponse expected = TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(traineeService.createTrainee(request)).thenReturn(expected);
        TraineeResponse actual = facade.createTrainee(request);

        assertEquals(expected, actual);
        verify(traineeService).createTrainee(request);
    }

    @Test
    void updateTrainee_delegatesToService() {
        TraineeUpdateRequest request = TraineeUpdateRequest.builder()
                .userId(1L)
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Kyiv")
                .build();
        TraineeResponse expected = TraineeResponse.builder()
                .userId(1L)
                .isActive(true)
                .build();

        when(traineeService.updateTrainee(request)).thenReturn(expected);
        TraineeResponse actual = facade.updateTrainee(request);

        assertEquals(expected, actual);
        verify(traineeService).updateTrainee(request);
    }

    @Test
    void deleteTrainee_delegatesToService() {
        facade.deleteTrainee(2L);

        verify(traineeService).deleteTrainee(2L);
    }

    @Test
    void getTraineeById_delegatesToService() {
        TraineeResponse expected = TraineeResponse.builder()
                .userId(1L)
                .build();

        when(traineeService.getTraineeById(1L)).thenReturn(expected);
        TraineeResponse actual = facade.getTraineeById(1L);

        assertEquals(expected, actual);
        verify(traineeService).getTraineeById(1L);
    }

    @Test
    void getTraineeByUsername_delegatesToService() {
        TraineeResponse expected = TraineeResponse.builder()
                .userId(1L)
                .username("john.doe")
                .build();

        when(traineeService.getTraineeByUsername("john.doe")).thenReturn(expected);
        TraineeResponse actual = facade.getTraineeByUsername("john.doe");

        assertEquals(expected, actual);
        verify(traineeService).getTraineeByUsername("john.doe");
    }

    @Test
    void createTrainer_delegatesToService() {
        TrainerCreateRequest request = TrainerCreateRequest.builder()
                .firstName("Anna")
                .lastName("Ivanova")
                .specialization("Yoga")
                .build();
        TrainerResponse expected = TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .specialization("Yoga")
                .build();

        when(trainerService.createTrainer(request)).thenReturn(expected);
        TrainerResponse actual = facade.createTrainer(request);

        assertEquals(expected, actual);
        verify(trainerService).createTrainer(request);
    }

    @Test
    void updateTrainer_delegatesToService() {
        TrainerUpdateRequest request = TrainerUpdateRequest.builder()
                .userId(2L)
                .isActive(true)
                .specialization("Pilates")
                .build();
        TrainerResponse expected = TrainerResponse.builder()
                .userId(2L)
                .isActive(true)
                .specialization("Pilates")
                .build();

        when(trainerService.updateTrainer(request)).thenReturn(expected);
        TrainerResponse actual = facade.updateTrainer(request);

        assertEquals(expected, actual);
        verify(trainerService).updateTrainer(request);
    }

    @Test
    void getTrainerById_delegatesToService() {
        TrainerResponse expected = TrainerResponse.builder()
                .userId(2L)
                .build();

        when(trainerService.getTrainerById(2L)).thenReturn(expected);
        TrainerResponse actual = facade.getTrainerById(2L);

        assertEquals(expected, actual);
        verify(trainerService).getTrainerById(2L);
    }

    @Test
    void getTrainerByUsername_delegatesToService() {
        TrainerResponse expected = TrainerResponse.builder()
                .userId(2L)
                .username("anna.ivanova")
                .build();

        when(trainerService.getTrainerByUsername("anna.ivanova")).thenReturn(expected);
        TrainerResponse actual = facade.getTrainerByUsername("anna.ivanova");

        assertEquals(expected, actual);
        verify(trainerService).getTrainerByUsername("anna.ivanova");
    }

    @Test
    void createTraining_delegatesToService() {
        TrainingType type = TrainingType.builder().name("Cardio").build();
        TrainingCreateRequest request = TrainingCreateRequest.builder()
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 30))
                .trainingDuration(Duration.ofMinutes(40))
                .trainingName("Morning Cardio")
                .trainingType(type)
                .build();
        TrainingResponse expected = TrainingResponse.builder()
                .id(3L)
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 30))
                .trainingDuration(Duration.ofMinutes(40))
                .trainingName("Morning Cardio")
                .trainingType(type)
                .build();

        when(trainingService.createTraining(request)).thenReturn(expected);
        TrainingResponse actual = facade.createTraining(request);

        assertEquals(expected, actual);
        verify(trainingService).createTraining(request);
    }

    @Test
    void getTrainingById_delegatesToService() {
        TrainingType type = TrainingType.builder().name("Strength").build();
        TrainingResponse expected = TrainingResponse.builder()
                .id(4L)
                .trainerId(11L)
                .traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(50))
                .trainingName("Evening Strength")
                .trainingType(type)
                .build();

        when(trainingService.getTrainingById(4L)).thenReturn(expected);
        TrainingResponse actual = facade.getTrainingById(4L);

        assertEquals(expected, actual);
        verify(trainingService).getTrainingById(4L);
    }
}