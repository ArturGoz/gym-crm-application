package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeGetDTO;
import com.gca.dto.trainee.TraineeTrainersUpdateDTO;
import com.gca.dto.trainee.TraineeUpdateRequestDTO;
import com.gca.dto.trainee.TraineeUpdateResponseDTO;
import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.dto.user.UserCredentialsDTO;
import com.gca.mapper.TraineeMapper;
import com.gca.mapper.TrainerMapper;
import com.gca.mapper.UserMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.User;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateRequest;
import com.gca.service.UserService;
import com.gca.service.common.CoreValidator;
import com.gca.service.common.UserProfileService;
import com.gca.utils.GymTestProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_success() {
        TraineeCreateDTO request = GymTestProvider.createTraineeCreateDTO();
        Trainee trainee = GymTestProvider.constructTrainee();
        Trainee traineeWithCreds = GymTestProvider.constructTrainee();
        UserCredentialsDTO expected = GymTestProvider.constructUserCreateDTO();

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(trainee.getUser());
        when(userProfileService.encryptPassword(trainee.getUser().getPassword())).thenReturn(expected.getPassword());
        when(mapper.toEntity(request)).thenReturn(trainee);
        when(dao.create(any(Trainee.class))).thenReturn(traineeWithCreds);
        when(userMapper.toResponse(any(User.class))).thenReturn(expected);

        UserCredentialsDTO actual = service.createTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getPassword() + "1", actual.getPassword());
        assertEquals(expected.getUsername() + "1", actual.getUsername());
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainee.class));
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    void updateTrainee_success() {
        TraineeUpdateRequestDTO updateRequest = GymTestProvider.createTraineeUpdateRequestDTO();
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
        TraineeUpdateResponseDTO expected =
                GymTestProvider.createTraineeUpdateResponse(updated);

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(existing);
        when(mapper.fillUserFields(existing.getUser(), updateRequest)).thenReturn(filledUser);
        when(mapper.fillTraineeFields(existing, filledUser, updateRequest)).thenReturn(filledExistingTrainee);
        when(dao.update(filledExistingTrainee)).thenReturn(updated);
        when(mapper.toUpdateResponse(updated)).thenReturn(expected);

        TraineeUpdateResponseDTO actual = service.updateTrainee(updateRequest);

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
        TraineeUpdateRequestDTO updateRequest = GymTestProvider.createTraineeUpdateRequestDTO();

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainee(updateRequest));
        assertEquals("Invalid trainee username: arnold.schwarzenegger", ex.getMessage());
    }

    @Test
    void getTraineeByUsername_success() {
        String username = "john_doe";
        Trainee mockTrainee = GymTestProvider.constructTrainee();
        TraineeGetDTO expectedResponse = GymTestProvider.createTraineeGetDTO();

        when(dao.findByUsername(username)).thenReturn(mockTrainee);
        when(mapper.toGetDto(mockTrainee)).thenReturn(expectedResponse);

        TraineeGetDTO actualResponse = service.getTraineeByUsername(username);

        assertEquals(expectedResponse, actualResponse);
        verify(validator).validateUsername(username);
        verify(dao).findByUsername(username);
        verify(mapper).toGetDto(mockTrainee);
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
        String username = "john.doe";
        TraineeAssignedTrainersUpdateRequest request = GymTestProvider.createTraineeAssignedTrainersUpdateRequest();
        TraineeTrainersUpdateDTO updateDTO = new TraineeTrainersUpdateDTO(username, request.getTrainerUsernames());

        Trainee updatedTrainee = GymTestProvider.constructTrainee();
        List<Trainer> trainers = new ArrayList<>(updatedTrainee.getTrainers());

        AssignedTrainerDTO trainer1 = GymTestProvider.createAssignedTrainerDTO("trainer1");
        AssignedTrainerDTO trainer2 = GymTestProvider.createAssignedTrainerDTO("trainer2");

        when(dao.updateTraineeTrainers(updateDTO.getTraineeUsername(), updateDTO.getTrainerNames()))
                .thenReturn(updatedTrainee);
        when(trainerMapper.toAssignedDto(trainers.get(0))).thenReturn(trainer1);
        when(trainerMapper.toAssignedDto(trainers.get(1))).thenReturn(trainer2);

        List<AssignedTrainerDTO> actual = service.updateTraineeTrainers(updateDTO);

        assertEquals(2, actual.size());
        assertTrue(actual.contains(trainer1));
        assertTrue(actual.contains(trainer2));
        verify(dao).updateTraineeTrainers(updateDTO.getTraineeUsername(), updateDTO.getTrainerNames());
        verify(trainerMapper).toAssignedDto(trainers.get(0));
        verify(trainerMapper).toAssignedDto(trainers.get(1));
    }
}