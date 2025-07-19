package com.gca.service.impl;

import com.gca.GymTestProvider;
import com.gca.dao.TraineeDAO;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateDTO;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserCreateDTO;
import com.gca.mapper.TraineeMapper;
import com.gca.mapper.UserMapper;
import com.gca.model.Trainee;
import com.gca.model.User;
import com.gca.service.UserService;
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
    private TraineeMapper mapper;

    @Mock
    private CoreValidator validator;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_success() {
        TraineeCreateDTO request = GymTestProvider.createTraineeCreateDTO();
        Trainee trainee = GymTestProvider.constructTrainee();
        Trainee traineeWithCreds = GymTestProvider.constructTrainee();
        UserCreateDTO expected = GymTestProvider.constructUserCreateDTO();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(trainee.getUser());
        when(mapper.toEntity(request)).thenReturn(trainee);
        when(dao.create(any(Trainee.class))).thenReturn(traineeWithCreds);
        when(userMapper.toResponse(any(User.class))).thenReturn(expected);

        UserCreateDTO actual = service.createTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainee.class));
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    void updateTrainee_success() {
        TraineeUpdateData updateRequest = GymTestProvider.createTraineeUpdateRequest();
        User filledUser = GymTestProvider.constructUser();
        Trainee existing = GymTestProvider.constructTrainee();

        Trainee filledExistingTrainee = existing.toBuilder()
                .user(filledUser)
                .address(updateRequest.getAddress())
                .dateOfBirth(updateRequest.getDateOfBirth())
                .build();
        Trainee updated = filledExistingTrainee.toBuilder()
                .id(existing.getId())
                .build();
        TraineeUpdateDTO expected =
                GymTestProvider.createTraineeUpdateResponse(updated);

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(existing);
        when(mapper.fillUserFields(existing.getUser(), updateRequest)).thenReturn(filledUser);
        when(mapper.fillTraineeFields(existing, filledUser, updateRequest)).thenReturn(filledExistingTrainee);
        when(dao.update(filledExistingTrainee)).thenReturn(updated);
        when(mapper.toUpdateResponse(updated)).thenReturn(expected);

        TraineeUpdateDTO actual = service.updateTrainee(updateRequest);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getIsActive(), actual.getIsActive());

        verify(dao).update(existing);
        verify(mapper).toUpdateResponse(updated);
    }

    @Test
    void updateTrainee_notFound_throwsException() {
        TraineeUpdateData updateRequest = GymTestProvider.createTraineeUpdateRequest();

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainee(updateRequest));
        assertEquals("Invalid trainee username: john.doe", ex.getMessage());
    }

    @Test
    void getTraineeByUsername_success() {
        String username = "john_doe";
        Trainee mockTrainee = GymTestProvider.constructTrainee();
        TraineeDTO expectedResponse = GymTestProvider.constructTraineeResponse();

        when(dao.findByUsername(username)).thenReturn(mockTrainee);
        when(mapper.toResponse(mockTrainee)).thenReturn(expectedResponse);

        TraineeDTO actualResponse = service.getTraineeByUsername(username);

        assertEquals(expectedResponse, actualResponse);
        verify(dao).findByUsername(username);
        verify(mapper).toResponse(mockTrainee);
    }

    @Test
    void deleteTraineeByUsername_shouldThrow_whenNotFound() {
        String username = "ghost";

        when(dao.findByUsername(username)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.deleteTraineeByUsername(username));

        assertEquals(("Trainee with username 'ghost' not found"), ex.getMessage());
        verify(dao).findByUsername(username);
    }

    @Test
    void updateTraineeTrainers_shouldUpdateAndReturnResponse() {
        UpdateTraineeTrainersRequest request = GymTestProvider.createUpdateTraineeTrainersRequest();
        Trainee updatedTrainee = GymTestProvider.constructTrainee();
        TraineeDTO expectedResponse = GymTestProvider.constructTraineeResponse();

        when(dao.updateTraineeTrainers(request.getTraineeUsername(), request.getTrainerNames()))
                .thenReturn(updatedTrainee);
        when(mapper.toResponse(updatedTrainee)).thenReturn(expectedResponse);

        TraineeDTO actualResponse = service.updateTraineeTrainers(request);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getFirstName(), actualResponse.getFirstName());
        assertEquals(expectedResponse.getLastName(), actualResponse.getLastName());
        verify(dao).updateTraineeTrainers(request.getTraineeUsername(), request.getTrainerNames());
        verify(mapper).toResponse(updatedTrainee);
    }

}