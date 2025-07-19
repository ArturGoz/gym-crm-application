package com.gca.facade;

import com.gca.utils.GymTestProvider;
import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateDTO;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerDTO;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateDTO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.mapper.rest.RestTraineeMapper;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
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

    @Mock
    private RestTraineeMapper restTraineeMapper;

    @InjectMocks
    private TrainingAppFacade facade;

    @Test
    void createTrainee_delegatesToService() {
        TraineeCreateDTO internalDto = GymTestProvider.createTraineeCreateDTO();
        UserCreateDTO userCreateDto = GymTestProvider.constructUserCreateDTO();
        TraineeCreateRequest restRequest = GymTestProvider.createTraineeCreateRequest();
        TraineeCreateResponse expectedResponse = GymTestProvider.createTraineeCreateResponse();

        when(restTraineeMapper.toDto(restRequest)).thenReturn(internalDto);
        when(traineeService.createTrainee(internalDto)).thenReturn(userCreateDto);
        when(restTraineeMapper.toRest(userCreateDto)).thenReturn(expectedResponse);

        TraineeCreateResponse actualResponse = facade.createTrainee(restRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(restTraineeMapper).toDto(restRequest);
        verify(traineeService).createTrainee(internalDto);
        verify(restTraineeMapper).toRest(userCreateDto);
    }

    @Test
    void updateTrainee_delegatesToService() {
        TraineeUpdateData request = GymTestProvider.createTraineeUpdateRequest();
        TraineeUpdateDTO expected = GymTestProvider.createTraineeUpdateResponse();

        when(traineeService.updateTrainee(request)).thenReturn(expected);

        TraineeUpdateDTO actual = facade.updateTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        verify(traineeService).updateTrainee(request);
    }

    @Test
    void createTrainer_delegatesToService() {
        TrainerCreateRequest request = GymTestProvider.createTrainerCreateRequest();
        UserCreateDTO expected = GymTestProvider.constructUserCreateDTO();

        when(trainerService.createTrainer(request)).thenReturn(expected);

        UserCreateDTO actual = facade.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(trainerService).createTrainer(request);
    }

    @Test
    void updateTrainer_delegatesToService() {
        TrainerUpdateRequest request = GymTestProvider.createTrainerUpdateRequest();
        TrainerUpdateDTO expected = GymTestProvider.createTrainerUpdateResponse();

        when(trainerService.updateTrainer(request)).thenReturn(expected);

        TrainerUpdateDTO actual = facade.updateTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getTrainees(), actual.getTrainees());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());

        verify(trainerService).updateTrainer(request);
    }

    @Test
    void createTraining_delegatesToService() {
        TrainingCreateRequest request = GymTestProvider.createTrainingCreateRequest();
        TrainingDTO expected = GymTestProvider.constructTrainingResponse();

        when(trainingService.createTraining(request)).thenReturn(expected);

        TrainingDTO actual = facade.createTraining(request);

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
        List<TrainingDTO> expected = List.of(GymTestProvider.constructTrainingResponse());

        when(trainingService.getTrainerTrainings(filter)).thenReturn(expected);

        List<TrainingDTO> actual = facade.findFilteredTrainings(filter);

        assertEquals(expected, actual);
        verify(trainingService).getTrainerTrainings(filter);
    }

    @Test
    void findFilteredTrainings_byTraineeCriteria_delegatesToService() {
        TrainingTraineeCriteriaFilter filter = GymTestProvider.buildTraineeCriteriaFilter();
        List<TrainingDTO> expected = List.of(GymTestProvider.constructTrainingResponse());

        when(trainingService.getTraineeTrainings(filter)).thenReturn(expected);

        List<TrainingDTO> actual = facade.findFilteredTrainings(filter);

        assertEquals(expected, actual);
        verify(trainingService).getTraineeTrainings(filter);
    }

    @Test
    void getTraineeByUsername_delegatesToService() {
        String username = "john.doe";
        TraineeDTO expected = GymTestProvider.constructTraineeResponse();

        when(traineeService.getTraineeByUsername(username)).thenReturn(expected);

        TraineeDTO actual = facade.getTraineeByUsername(username);

        assertEquals(expected, actual);
        verify(traineeService).getTraineeByUsername(username);
    }

    @Test
    void getTrainerByUsername_delegatesToService() {
        String username = "trainer.one";
        TrainerDTO expected = GymTestProvider.constructTrainerResponse();

        when(trainerService.getTrainerByUsername(username)).thenReturn(expected);

        TrainerDTO actual = facade.getTrainerByUsername(username);

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

        facade.toggleUserActiveStatus(username);

        verify(userService).toggleActiveStatus(username);
    }

    @Test
    void getUnassignedTrainers_delegatesToService() {
        String traineeUsername = "john.doe";

        TrainerDTO response1 = GymTestProvider.constructTrainerResponse();
        TrainerDTO response2 = GymTestProvider.constructTrainerResponse();

        List<TrainerDTO> expected = List.of(response1, response2);

        when(trainerService.getUnassignedTrainers(traineeUsername)).thenReturn(expected);

        List<TrainerDTO> actual = facade.getUnassignedTrainers(traineeUsername);

        assertEquals(expected, actual);
        verify(trainerService).getUnassignedTrainers(traineeUsername);
    }

    @Test
    void updateTraineeTrainers_delegatesToService() {
        UpdateTraineeTrainersRequest request = GymTestProvider.createUpdateTraineeTrainersRequest();
        TraineeDTO expected = GymTestProvider.constructTraineeResponse();

        when(traineeService.updateTraineeTrainers(request)).thenReturn(expected);

        TraineeDTO actual = facade.updateTraineeTrainers(request);

        assertEquals(expected, actual);
        verify(traineeService).updateTraineeTrainers(request);
    }
}