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
                .firstName("John").lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Kyiv")
                .build();
        TraineeResponse expected = TraineeResponse.builder().userId(1L).firstName("John").lastName("Doe").build();

        when(traineeService.createTrainee(request)).thenReturn(expected);

        TraineeResponse result = facade.createTrainee(request);

        assertEquals(expected, result);
        verify(traineeService).createTrainee(request);
    }

    @Test
    void updateTrainee_delegatesToService() {
        TraineeUpdateRequest request = TraineeUpdateRequest.builder()
                .userId(1L).isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("Kyiv").build();
        TraineeResponse expected = TraineeResponse.builder().userId(1L).isActive(true).build();

        when(traineeService.updateTrainee(request)).thenReturn(expected);

        TraineeResponse result = facade.updateTrainee(request);

        assertEquals(expected, result);
        verify(traineeService).updateTrainee(request);
    }

    @Test
    void deleteTrainee_delegatesToService() {
        facade.deleteTrainee(2L);
        verify(traineeService).deleteTrainee(2L);
    }

    @Test
    void getTraineeById_delegatesToService() {
        TraineeResponse expected = TraineeResponse.builder().userId(1L).build();
        when(traineeService.getTraineeById(1L)).thenReturn(expected);

        TraineeResponse result = facade.getTraineeById(1L);

        assertEquals(expected, result);
        verify(traineeService).getTraineeById(1L);
    }

    @Test
    void getTraineeByUsername_delegatesToService() {
        TraineeResponse expected = TraineeResponse.builder().userId(1L).username("john.doe").build();
        when(traineeService.getTraineeByUsername("john.doe")).thenReturn(expected);

        TraineeResponse result = facade.getTraineeByUsername("john.doe");

        assertEquals(expected, result);
        verify(traineeService).getTraineeByUsername("john.doe");
    }

    @Test
    void createTrainer_delegatesToService() {
        TrainerCreateRequest request = TrainerCreateRequest.builder()
                .firstName("Anna").lastName("Ivanova").specialization("Yoga").build();
        TrainerResponse expected = TrainerResponse.builder().userId(2L).firstName("Anna").lastName("Ivanova").specialization("Yoga").build();

        when(trainerService.createTrainer(request)).thenReturn(expected);

        TrainerResponse result = facade.createTrainer(request);

        assertEquals(expected, result);
        verify(trainerService).createTrainer(request);
    }

    @Test
    void updateTrainer_delegatesToService() {
        TrainerUpdateRequest request = TrainerUpdateRequest.builder().userId(2L).isActive(true).specialization("Pilates").build();
        TrainerResponse expected = TrainerResponse.builder().userId(2L).isActive(true).specialization("Pilates").build();

        when(trainerService.updateTrainer(request)).thenReturn(expected);

        TrainerResponse result = facade.updateTrainer(request);

        assertEquals(expected, result);
        verify(trainerService).updateTrainer(request);
    }

    @Test
    void getTrainerById_delegatesToService() {
        TrainerResponse expected = TrainerResponse.builder().userId(2L).build();
        when(trainerService.getTrainerById(2L)).thenReturn(expected);

        TrainerResponse result = facade.getTrainerById(2L);

        assertEquals(expected, result);
        verify(trainerService).getTrainerById(2L);
    }

    @Test
    void getTrainerByUsername_delegatesToService() {
        TrainerResponse expected = TrainerResponse.builder().userId(2L).username("anna.ivanova").build();
        when(trainerService.getTrainerByUsername("anna.ivanova")).thenReturn(expected);

        TrainerResponse result = facade.getTrainerByUsername("anna.ivanova");

        assertEquals(expected, result);
        verify(trainerService).getTrainerByUsername("anna.ivanova");
    }

    @Test
    void createTraining_delegatesToService() {
        TrainingType type = TrainingType.builder().name("Cardio").build();
        TrainingCreateRequest request = TrainingCreateRequest.builder()
                .trainerId(10L).traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 30))
                .trainingDuration(Duration.ofMinutes(40))
                .trainingName("Morning Cardio")
                .trainingType(type)
                .build();

        TrainingResponse expected = TrainingResponse.builder()
                .id(3L)
                .trainerId(10L).traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 30))
                .trainingDuration(Duration.ofMinutes(40))
                .trainingName("Morning Cardio")
                .trainingType(type)
                .build();

        when(trainingService.createTraining(request)).thenReturn(expected);

        TrainingResponse result = facade.createTraining(request);

        assertEquals(expected, result);
        verify(trainingService).createTraining(request);
    }

    @Test
    void getTrainingById_delegatesToService() {
        TrainingType type = TrainingType.builder().name("Strength").build();
        TrainingResponse expected = TrainingResponse.builder()
                .id(4L)
                .trainerId(11L).traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(50))
                .trainingName("Evening Strength")
                .trainingType(type)
                .build();

        when(trainingService.getTrainingById(4L)).thenReturn(expected);

        TrainingResponse result = facade.getTrainingById(4L);

        assertEquals(expected, result);
        verify(trainingService).getTrainingById(4L);
    }
}
