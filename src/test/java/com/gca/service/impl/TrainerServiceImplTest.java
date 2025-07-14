package com.gca.service.impl;

import com.gca.GymTestProvider;
import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateResponse;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserCreationResponse;
import com.gca.mapper.TrainerMapper;
import com.gca.mapper.UserMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import com.gca.service.UserService;
import com.gca.service.common.CoreValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerDAO dao;

    @Mock
    private CoreValidator validator;

    @Mock
    private TrainerMapper mapper;

    @Mock
    private TraineeDAO trainerDAO;

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TrainerServiceImpl service;

    @Test
    void createTrainer_success() {
        TrainerCreateRequest request = GymTestProvider.createTrainerCreateRequest();
        Trainer trainer = GymTestProvider.constructTrainer();
        Trainer trainerWithCreds = GymTestProvider.constructTrainer();
        UserCreationResponse expected = GymTestProvider.constructUserCreationResponse();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(trainer.getUser());
        when(trainingTypeDAO.getById(any(Long.class))).thenReturn(TrainingType.builder().name("Yoga").build());
        when(dao.create(any(Trainer.class))).thenReturn(trainerWithCreds);
        when(userMapper.toResponse(any(User.class))).thenReturn(expected);

        UserCreationResponse actual = service.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(dao).create(any(Trainer.class));
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    void updateTrainer_success() {
        TrainerUpdateRequest updateRequest = GymTestProvider.createTrainerUpdateRequest();
        Trainer existing = GymTestProvider.constructInactiveTrainer();
        Trainer updated = GymTestProvider.constructUpdatedTrainer();
        TrainerUpdateResponse expected = GymTestProvider.createTrainerUpdateResponse();

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(existing);
        when(dao.update(existing)).thenReturn(updated);
        when(mapper.toUpdateResponse(updated)).thenReturn(expected);

        TrainerUpdateResponse actual = service.updateTrainer(updateRequest);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getSpecializationId(), actual.getSpecializationId());
        assertEquals(expected.getTrainees().size(), actual.getTrainees().size());

        verify(dao).findByUsername(updateRequest.getUsername());
        verify(dao).update(existing);
        verify(mapper).toUpdateResponse(updated);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        TrainerUpdateRequest updateRequest = GymTestProvider.createTrainerUpdateRequest();

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainer(updateRequest));

        assertEquals("Invalid trainer username: john.doe", ex.getMessage());
    }

    @Test
    void getTrainerByUsername_success() {
        String username = "john_doe";
        Trainer mockTrainer = GymTestProvider.constructTrainer();
        TrainerResponse expectedResponse = GymTestProvider.constructTrainerResponse();

        when(dao.findByUsername(username)).thenReturn(mockTrainer);
        when(mapper.toResponse(mockTrainer)).thenReturn(expectedResponse);

        TrainerResponse actualResponse = service.getTrainerByUsername(username);

        assertEquals(expectedResponse, actualResponse);

        verify(dao).findByUsername(username);
        verify(mapper).toResponse(mockTrainer);
    }

    @Test
    void getUnassignedTrainers_shouldReturnCorrectList() {
        String traineeUsername = "john_doe";

        doNothing().when(validator).validateUsername(traineeUsername);

        Trainer assignedTrainer = GymTestProvider.constructTrainer().toBuilder().id(1L).build();
        Trainer unassignedTrainer = GymTestProvider.constructTrainer();

        Set<Trainer> assigned = Set.of(assignedTrainer);
        List<Trainer> allTrainers = List.of(assignedTrainer, unassignedTrainer);

        Trainee trainee = GymTestProvider.constructTrainee();
        trainee.setTrainers(assigned);

        TrainerResponse unassignedTrainerResponse = GymTestProvider.constructTrainerResponse();

        when(trainerDAO.findByUsername(traineeUsername)).thenReturn(trainee);
        when(dao.getAllTrainers()).thenReturn(allTrainers);
        when(mapper.toResponse(unassignedTrainer)).thenReturn(unassignedTrainerResponse);

        List<TrainerResponse> actual = service.getUnassignedTrainers(traineeUsername);

        assertEquals(1, actual.size());
        assertTrue(actual.contains(unassignedTrainerResponse));

        verify(validator).validateUsername(traineeUsername);
        verify(mapper).toResponse(unassignedTrainer);
        verify(mapper, never()).toResponse(assignedTrainer);
    }
}
