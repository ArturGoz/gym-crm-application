package com.gca.facade;

import com.gca.GymTestProvider;
import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
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
import com.gca.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingAppFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainingAppFacade facade;

    @Test
    void createTrainee_delegatesToService() {
        TraineeCreateRequest request = GymTestProvider.createTraineeCreateRequest();
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(traineeService.createTrainee(request)).thenReturn(expected);

        TraineeResponse actual = facade.createTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        verify(traineeService).createTrainee(request);
    }

    @Test
    void updateTrainee_delegatesToService() {
        TraineeUpdateRequest request = GymTestProvider.createTraineeUpdateRequest();
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(traineeService.updateTrainee(request)).thenReturn(expected);

        TraineeResponse actual = facade.updateTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        verify(traineeService).updateTrainee(request);
    }

    @Test
    void deleteTrainee_delegatesToService() {
        facade.deleteTrainee(2L);

        verify(traineeService).deleteTraineeById(2L);
    }

    @Test
    void getTraineeById_delegatesToService() {
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(traineeService.getTraineeById(1L)).thenReturn(expected);

        TraineeResponse actual = facade.getTraineeById(1L);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        verify(traineeService).getTraineeById(1L);
    }

    @Test
    void createTrainer_delegatesToService() {
        TrainerCreateRequest request = GymTestProvider.createTrainerCreateRequest();
        TrainerResponse expected = GymTestProvider.constructTrainerResponse();

        when(trainerService.createTrainer(request)).thenReturn(expected);

        TrainerResponse actual = facade.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(trainerService).createTrainer(request);
    }

    @Test
    void updateTrainer_delegatesToService() {
        TrainerUpdateRequest request = GymTestProvider.createTrainerUpdateRequest();
        TrainerResponse expected = GymTestProvider.constructUpdatedTrainerResponse();

        when(trainerService.updateTrainer(request)).thenReturn(expected);

        TrainerResponse actual = facade.updateTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(trainerService).updateTrainer(request);
    }

    @Test
    void getTrainerById_delegatesToService() {
        TrainerResponse expected = GymTestProvider.constructTrainerResponse();

        when(trainerService.getTrainerById(2L)).thenReturn(expected);

        TrainerResponse actual = facade.getTrainerById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(trainerService).getTrainerById(2L);
    }

    @Test
    void createTraining_delegatesToService() {
        TrainingCreateRequest request = GymTestProvider.createTrainingCreateRequest();
        TrainingResponse expected = GymTestProvider.constructTrainingResponse();

        when(trainingService.createTraining(request)).thenReturn(expected);

        TrainingResponse actual = facade.createTraining(request);

        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDate(), actual.getDate());
        verify(trainingService).createTraining(request);
    }

    @Test
    void getTrainingById_delegatesToService() {
        TrainingResponse expected = GymTestProvider.constructStrengthTrainingResponse();

        when(trainingService.getTrainingById(2L)).thenReturn(expected);

        TrainingResponse actual = facade.getTrainingById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDate(), actual.getDate());
        verify(trainingService).getTrainingById(2L);
    }

    @Test
    void changePassword_delegatesToUserService() {
        PasswordChangeRequest request =
                new PasswordChangeRequest(1L, "newSecretPassword");

        facade.changePassword(request);

        verify(userService).changeUserPassword(request);
    }

    @Test
    void findFilteredTrainings_byTrainerCriteria_delegatesToService() {
        TrainingTrainerCriteriaFilter filter = GymTestProvider.buildTrainerCriteriaFilter();
        List<TrainingResponse> expected = List.of(GymTestProvider.constructTrainingResponse());

        when(trainingService.getTrainerTrainings(filter)).thenReturn(expected);

        List<TrainingResponse> actual = facade.findFilteredTrainings(filter);

        assertEquals(expected, actual);
        verify(trainingService).getTrainerTrainings(filter);
    }

    @Test
    void findFilteredTrainings_byTraineeCriteria_delegatesToService() {
        TrainingTraineeCriteriaFilter filter = GymTestProvider.buildTraineeCriteriaFilter();
        List<TrainingResponse> expected = List.of(GymTestProvider.constructTrainingResponse());

        when(trainingService.getTraineeTrainings(filter)).thenReturn(expected);

        List<TrainingResponse> actual = facade.findFilteredTrainings(filter);

        assertEquals(expected, actual);
        verify(trainingService).getTraineeTrainings(filter);
    }
}