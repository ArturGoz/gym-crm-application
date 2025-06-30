package com.gca.facade;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.service.TraineeService;
import com.gca.service.TrainerService;
import com.gca.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.gca.GymTestProvider;

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
        TraineeCreateRequest request = GymTestProvider.traineeCreateRequest();
        TraineeResponse expected = GymTestProvider.traineeResponse();

        when(traineeService.createTrainee(request)).thenReturn(expected);
        TraineeResponse actual = facade.createTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(traineeService).createTrainee(request);
    }

    @Test
    void updateTrainee_delegatesToService() {
        TraineeUpdateRequest request = GymTestProvider.traineeUpdateRequest();
        TraineeResponse expected = GymTestProvider.traineeResponse();

        when(traineeService.updateTrainee(request)).thenReturn(expected);
        TraineeResponse actual = facade.updateTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(traineeService).updateTrainee(request);
    }

    @Test
    void deleteTrainee_delegatesToService() {
        facade.deleteTrainee(2L);

        verify(traineeService).deleteTrainee(2L);
    }

    @Test
    void getTraineeById_delegatesToService() {
        TraineeResponse expected = GymTestProvider.traineeResponse();

        when(traineeService.getTraineeById(1L)).thenReturn(expected);
        TraineeResponse actual = facade.getTraineeById(1L);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(traineeService).getTraineeById(1L);
    }

    @Test
    void getTraineeByUsername_delegatesToService() {
        TraineeResponse expected = GymTestProvider.traineeResponse();

        when(traineeService.getTraineeByUsername("john.doe")).thenReturn(expected);
        TraineeResponse actual = facade.getTraineeByUsername("john.doe");

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(traineeService).getTraineeByUsername("john.doe");
    }

    @Test
    void createTrainer_delegatesToService() {
        TrainerCreateRequest request = GymTestProvider.trainerCreateRequest();
        TrainerResponse expected = GymTestProvider.trainerResponse();

        when(trainerService.createTrainer(request)).thenReturn(expected);
        TrainerResponse actual = facade.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(trainerService).createTrainer(request);
    }

    @Test
    void updateTrainer_delegatesToService() {
        TrainerUpdateRequest request = GymTestProvider.trainerUpdateRequest();
        TrainerResponse expected = GymTestProvider.trainerResponseUpdated();

        when(trainerService.updateTrainer(request)).thenReturn(expected);
        TrainerResponse actual = facade.updateTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(trainerService).updateTrainer(request);
    }

    @Test
    void getTrainerById_delegatesToService() {
        TrainerResponse expected = GymTestProvider.trainerResponse();

        when(trainerService.getTrainerById(2L)).thenReturn(expected);
        TrainerResponse actual = facade.getTrainerById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(trainerService).getTrainerById(2L);
    }

    @Test
    void getTrainerByUsername_delegatesToService() {
        TrainerResponse expected = GymTestProvider.trainerResponse();

        when(trainerService.getTrainerByUsername("anna.ivanova")).thenReturn(expected);
        TrainerResponse actual = facade.getTrainerByUsername("anna.ivanova");

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(trainerService).getTrainerByUsername("anna.ivanova");
    }

    @Test
    void createTraining_delegatesToService() {
        TrainingCreateRequest request = GymTestProvider.trainingCreateRequest();
        TrainingResponse expected = GymTestProvider.trainingResponse();

        when(trainingService.createTraining(request)).thenReturn(expected);
        TrainingResponse actual = facade.createTraining(request);

        assertEquals(expected, actual);
        assertEquals(expected.getTrainingType(), actual.getTrainingType());
        assertEquals(expected.getTrainingDate(), actual.getTrainingDate());
        verify(trainingService).createTraining(request);
    }

    @Test
    void getTrainingById_delegatesToService() {
        TrainingResponse expected = GymTestProvider.trainingResponseStrength();

        when(trainingService.getTrainingById(2L)).thenReturn(expected);
        TrainingResponse actual = facade.getTrainingById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getTrainingType(), actual.getTrainingType());
        assertEquals(expected.getTrainingDate(), actual.getTrainingDate());
        verify(trainingService).getTrainingById(2L);
    }
}