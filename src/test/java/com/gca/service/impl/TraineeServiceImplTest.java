package com.gca.service.impl;

import com.gca.GymTestProvider;
import com.gca.dao.TraineeDAO;
import com.gca.dao.UserDAO;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.exception.ServiceException;
import com.gca.mapper.TraineeMapper;
import com.gca.model.Trainee;
import com.gca.service.common.CoreValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeDAO dao;

    @Mock
    private UserDAO userDAO;

    @Mock
    private TraineeMapper mapper;

    @Mock
    private CoreValidator validator;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_success() {
        TraineeCreateRequest request = GymTestProvider.createTraineeCreateRequest();
        Trainee trainee = GymTestProvider.constructTrainee();
        Trainee traineeWithCreds = GymTestProvider.constructTrainee();
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(userDAO.getById(any(Long.class))).thenReturn(trainee.getUser());
        when(mapper.toEntity(request)).thenReturn(trainee);
        when(dao.create(any(Trainee.class))).thenReturn(traineeWithCreds);
        when(mapper.toResponse(any(Trainee.class))).thenReturn(expected);

        TraineeResponse actual = service.createTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainee.class));
        verify(mapper).toResponse(any(Trainee.class));
    }

    @Test
    void updateTrainee_success() {
        TraineeUpdateRequest updateRequest = GymTestProvider.createTraineeUpdateRequest();
        Trainee existing = GymTestProvider.constructTrainee().toBuilder().address("1232").build();
        Trainee updated = existing.toBuilder().address("Kyiv, Khreschatyk 10").build();
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(dao.getById(1L)).thenReturn(existing);
        when(mapper.toEntity(updateRequest)).thenReturn(updated);
        when(dao.update(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(expected);

        TraineeResponse actual = service.updateTrainee(updateRequest);

        assertEquals(expected, actual);
        verify(dao).getById(1L);
        verify(dao).update(existing);
        verify(mapper).toResponse(updated);
    }

    @Test
    void updateTrainee_notFound_throwsException() {
        TraineeUpdateRequest updateRequest = TraineeUpdateRequest.builder()
                .id(2L)
                .build();

        when(dao.getById(2L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainee(updateRequest));
        assertEquals("Invalid trainee ID: 2", ex.getMessage());
    }

    @Test
    void getTraineeByUsername_success() {
        String username = "john_doe";
        Trainee mockTrainee = GymTestProvider.constructTrainee();
        TraineeResponse expectedResponse = GymTestProvider.constructTraineeResponse();

        when(dao.findByUsername(username)).thenReturn(mockTrainee);
        when(mapper.toResponse(mockTrainee)).thenReturn(expectedResponse);

        TraineeResponse actualResponse = service.getTraineeByUsername(username);

        assertEquals(expectedResponse, actualResponse);

        verify(dao).findByUsername(username);
        verify(mapper).toResponse(mockTrainee);
    }

    @Test
    void createTrainee_shouldThrow_whenUserNotFound() {
        TraineeCreateRequest request = GymTestProvider.createTraineeCreateRequest();

        when(userDAO.getById(request.getUserId())).thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class, () -> service.createTrainee(request));

        assertEquals(("Invalid user ID: 1"), ex.getMessage());
        verify(userDAO).getById(request.getUserId());
    }

    @Test
    void deleteTraineeByUsername_shouldThrow_whenNotFound() {
        String username = "ghost";

        when(dao.findByUsername(username)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.deleteTraineeByUsername(username));

        assertEquals(("Trainee with username 'ghost' not found"), ex.getMessage());
        verify(dao).findByUsername(username);
    }
}