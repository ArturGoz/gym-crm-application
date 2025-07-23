package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.dto.trainer.TrainerCreateDTO;
import com.gca.dto.trainer.TrainerGetDTO;
import com.gca.dto.trainer.TrainerUpdateRequestDTO;
import com.gca.dto.trainer.TrainerUpdateResponseDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.dto.user.UserCredentialsDTO;
import com.gca.mapper.TrainerMapper;
import com.gca.mapper.UserMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import com.gca.service.UserService;
import com.gca.service.common.CoreValidator;
import com.gca.utils.GymTestProvider;
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
        TrainerCreateDTO request = GymTestProvider.createTrainerCreateDTO();
        Trainer trainer = GymTestProvider.constructTrainer();
        Trainer trainerWithCreds = GymTestProvider.constructTrainer();
        UserCredentialsDTO expected = GymTestProvider.constructUserCreateDTO();

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(trainer.getUser());
        when(trainingTypeDAO.getByName(any(String.class))).thenReturn(TrainingType.builder().name("Yoga").build());
        when(dao.create(any(Trainer.class))).thenReturn(trainerWithCreds);
        when(userMapper.toResponse(any(User.class))).thenReturn(expected);

        UserCredentialsDTO actual = service.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getUsername(), actual.getUsername());
        verify(dao).create(any(Trainer.class));
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    void updateTrainer_success() {
        TrainerUpdateRequestDTO updateRequest = GymTestProvider.createTrainerUpdateRequestDTO();
        Trainer existing = GymTestProvider.constructInactiveTrainer();

        User filledUser = GymTestProvider.constructUser();
        TrainingType filledTrainingType = GymTestProvider.createTrainingTypeStrength();

        Trainer filledTrainer = existing.toBuilder()
                .specialization(filledTrainingType)
                .user(filledUser)
                .build();
        Trainer updated = filledTrainer.toBuilder()
                .id(existing.getId())
                .build();
        TrainerUpdateResponseDTO expected =
                GymTestProvider.createTrainerUpdateResponseDTO(updated);

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(existing);
        when(mapper.fillUserFields(existing.getUser(), updateRequest)).thenReturn(filledUser);
        when(trainingTypeDAO.getByName(updateRequest.getSpecialization())).thenReturn(filledTrainingType);
        when(mapper.fillTrainerFields(existing, filledUser, filledTrainingType)).thenReturn(filledTrainer);
        when(dao.update(filledTrainer)).thenReturn(updated);
        when(mapper.toUpdateResponse(updated)).thenReturn(expected);

        TrainerUpdateResponseDTO actual = service.updateTrainer(updateRequest);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        assertEquals(expected.getTrainees().size(), actual.getTrainees().size());

        verify(dao).findByUsername(updateRequest.getUsername());
        verify(dao).update(existing);
        verify(mapper).toUpdateResponse(updated);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        TrainerUpdateRequestDTO updateRequest = GymTestProvider.createTrainerUpdateRequestDTO();

        when(dao.findByUsername(updateRequest.getUsername())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainer(updateRequest));

        assertEquals("Invalid trainer username: arnold.schwarzenegger", ex.getMessage());
    }

    @Test
    void getTrainerByUsername_success() {
        String username = "arnold.schwarzenegger";
        Trainer mockTrainer = GymTestProvider.constructTrainer();
        TrainerGetDTO expectedResponse = GymTestProvider.createTrainerGetDTO();

        when(dao.findByUsername(username)).thenReturn(mockTrainer);
        when(mapper.toGetDto(mockTrainer)).thenReturn(expectedResponse);

        TrainerGetDTO actualResponse = service.getTrainerByUsername(username);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedResponse.getLastName(), actualResponse.getLastName());
        assertEquals(expectedResponse.getSpecialization(), actualResponse.getSpecialization());
        verify(dao).findByUsername(username);
        verify(mapper).toGetDto(mockTrainer);
    }

    @Test
    void getUnassignedTrainers_shouldReturnCorrectList() {
        String traineeUsername = "arnold.schwarzenegger";

        doNothing().when(validator).validateUsername(traineeUsername);

        Trainer assignedTrainer = GymTestProvider.constructTrainer().toBuilder().id(1L).build();
        Trainer unassignedTrainer = GymTestProvider.constructTrainer();

        Set<Trainer> assigned = Set.of(assignedTrainer);
        List<Trainer> allTrainers = List.of(assignedTrainer, unassignedTrainer);

        Trainee trainee = GymTestProvider.constructTrainee();
        trainee.setTrainers(assigned);

        AssignedTrainerDTO unassignedAssignedTrainerDTO = GymTestProvider.createAssignedTrainerDTO();

        when(trainerDAO.findByUsername(traineeUsername)).thenReturn(trainee);
        when(dao.getAllTrainers()).thenReturn(allTrainers);
        when(mapper.toAssignedDto(unassignedTrainer)).thenReturn(unassignedAssignedTrainerDTO);

        List<AssignedTrainerDTO> actual = service.getUnassignedTrainers(traineeUsername);

        assertEquals(1, actual.size());
        assertTrue(actual.contains(unassignedAssignedTrainerDTO));
        verify(validator).validateUsername(traineeUsername);
        verify(mapper).toAssignedDto(unassignedTrainer);
        verify(mapper, never()).toAssignedDto(assignedTrainer);
    }
}
