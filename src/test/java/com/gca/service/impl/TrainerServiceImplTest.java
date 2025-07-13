package com.gca.service.impl;

import com.gca.GymTestProvider;
import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.UserDAO;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.mapper.TrainerMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.service.common.CoreValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private UserDAO userDAO;

    @Mock
    private CoreValidator validator;

    @Mock
    private TrainerMapper mapper;

    @Mock
    private TraineeDAO trainerDAO;

    @InjectMocks
    private TrainerServiceImpl service;

    @Test
    void createTrainer_success() {
        TrainerCreateRequest request = GymTestProvider.createTrainerCreateRequest();
        Trainer trainer = GymTestProvider.constructTrainer();
        Trainer trainerWithCreds = GymTestProvider.constructTrainer();
        TrainerResponse expected = GymTestProvider.constructTrainerResponse();

        when(userDAO.getById(any(Long.class))).thenReturn(trainer.getUser());
        when(mapper.toEntity(request)).thenReturn(trainer);
        when(dao.create(any(Trainer.class))).thenReturn(trainerWithCreds);
        when(mapper.toResponse(any(Trainer.class))).thenReturn(expected);

        TrainerResponse actual = service.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainer.class));
        verify(mapper).toResponse(any(Trainer.class));
    }

    @Test
    void updateTrainer_success() {
        TrainerUpdateRequest updateRequest = GymTestProvider.createTrainerUpdateRequest();
        Trainer existing = GymTestProvider.constructInactiveTrainer();
        Trainer updated = GymTestProvider.constructUpdatedTrainer();
        TrainerResponse expected = GymTestProvider.constructUpdatedTrainerResponse();

        when(dao.getById(2L)).thenReturn(existing);
        when(mapper.toEntity(updateRequest)).thenReturn(updated);
        when(dao.update(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(expected);

        TrainerResponse actual = service.updateTrainer(updateRequest);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(dao).getById(2L);
        verify(dao).update(existing);
        verify(mapper).toResponse(updated);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        TrainerUpdateRequest updateRequest = GymTestProvider.createTrainerUpdateRequestNotFound();

        when(dao.getById(3L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainer(updateRequest));

        assertEquals("Invalid trainer ID: 3", ex.getMessage());
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
