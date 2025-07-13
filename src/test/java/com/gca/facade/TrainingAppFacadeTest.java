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
import com.gca.dto.user.UserResponse;
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

    @Test
    void getTraineeByUsername_delegatesToService() {
        String username = "john.doe";
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(traineeService.getTraineeByUsername(username)).thenReturn(expected);

        TraineeResponse actual = facade.getTraineeByUsername(username);

        assertEquals(expected, actual);
        verify(traineeService).getTraineeByUsername(username);
    }

    @Test
    void getTrainerByUsername_delegatesToService() {
        String username = "trainer.one";
        TrainerResponse expected = GymTestProvider.constructTrainerResponse();

        when(trainerService.getTrainerByUsername(username)).thenReturn(expected);

        TrainerResponse actual = facade.getTrainerByUsername(username);

        assertEquals(expected, actual);
        verify(trainerService).getTrainerByUsername(username);
    }

    @Test
    void deleteTraineeByUsername_delegatesToService() {
        String username = "john.doe";

        facade.deleteTraineeByUsername(username);

        verify(traineeService).deleteTraineeByUsername(username);
    }

    @Test
    void toggleUserActiveStatus_delegatesToService() {
        String username = "john.doe";
        UserResponse expected = GymTestProvider.constructUserResponse();

        when(userService.toggleActiveStatus(username)).thenReturn(expected);

        UserResponse actual = facade.toggleUserActiveStatus(username);

        assertEquals(expected, actual);
        verify(userService).toggleActiveStatus(username);
    }

    @Test
    void getUnassignedTrainers_delegatesToService() {
        String traineeUsername = "john.doe";

        TrainerResponse response1 = GymTestProvider.constructTrainerResponse();
        TrainerResponse response2 = GymTestProvider.constructTrainerResponse();

        List<TrainerResponse> expected = List.of(response1, response2);

        when(trainerService.getUnassignedTrainers(traineeUsername)).thenReturn(expected);

        List<TrainerResponse> actual = facade.getUnassignedTrainers(traineeUsername);

        assertEquals(expected, actual);
        verify(trainerService).getUnassignedTrainers(traineeUsername);
    }
}