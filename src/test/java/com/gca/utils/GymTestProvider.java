package com.gca.utils;

import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.AssignedTraineeDTO;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeGetDTO;
import com.gca.dto.trainee.TraineeTrainersUpdateDTO;
import com.gca.dto.trainee.TraineeUpdateRequestDTO;
import com.gca.dto.trainee.TraineeUpdateResponseDTO;
import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.dto.trainer.TrainerCreateDTO;
import com.gca.dto.trainer.TrainerUpdateRequestDTO;
import com.gca.dto.trainer.TrainerUpdateResponseDTO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.dto.user.UserCredentialsDTO;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;
import com.gca.openapi.model.AssignedTrainerResponse;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateRequest;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateResponse;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
import com.gca.openapi.model.TraineeGetResponse;
import com.gca.openapi.model.TraineeUpdateRequest;
import com.gca.openapi.model.TraineeUpdateResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class GymTestProvider {

    public static TraineeGetResponse createTraineeGetResponse() {
        TraineeGetResponse response = new TraineeGetResponse();
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setIsActive(true);
        response.setAddress("Kyiv, Khreschatyk 10");
        response.setDateOfBirth(LocalDate.of(1990, 1, 1));
        response.setTrainers(List.of(createAssignedTrainerResponse()));

        return response;
    }

    public static TraineeCreateResponse createTraineeCreateResponse() {
        TraineeCreateResponse traineeCreateResponse = new TraineeCreateResponse();
        traineeCreateResponse.setUsername("john.doe");
        traineeCreateResponse.setPassword("password");

        return traineeCreateResponse;
    }

    public static TraineeCreateRequest createTraineeCreateRequest() {
        TraineeCreateRequest request = new TraineeCreateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setAddress("Kyiv, Shevchenka 1");

        return request;
    }

    public static TraineeTrainersUpdateDTO createUpdateTraineeTrainersRequest() {
        return TraineeTrainersUpdateDTO.builder()
                .traineeUsername("john_doe")
                .trainerNames(List.of("trainer1", "trainer2"))
                .build();
    }

    public static TrainerUpdateResponseDTO createTrainerUpdateResponse() {
        return TrainerUpdateResponseDTO.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .username("username")
                .isActive(true)
                .trainees(List.of(createAssignedTraineeDTO()))
                .specialization("Yoga")
                .build();
    }

    public static TrainerUpdateResponseDTO createTrainerUpdateResponse(Trainer trainer) {
        return TrainerUpdateResponseDTO.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUsername())
                .isActive(trainer.getUser().getIsActive())
                .trainees(List.of(createAssignedTraineeDTO()))
                .specialization(trainer.getSpecialization().getName())
                .build();
    }

    public static TraineeUpdateResponseDTO createTraineeUpdateResponse() {
        return TraineeUpdateResponseDTO.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .username("username")
                .isActive(true)
                .trainers(List.of(createAssignedTrainerDTO()))
                .address("Address")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    public static TraineeUpdateResponseDTO createTraineeUpdateResponse(Trainee trainee) {
        return TraineeUpdateResponseDTO.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .isActive(trainee.getUser().getIsActive())
                .trainers(List.of(createAssignedTrainerDTO()))
                .address(trainee.getAddress())
                .dateOfBirth(trainee.getDateOfBirth())
                .build();
    }

    public static TrainerCreateDTO createTrainerCreateRequest() {
        return TrainerCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .specialization("Boxing")
                .build();
    }

    public static TrainerUpdateRequestDTO createTrainerUpdateRequest() {
        return TrainerUpdateRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .username("john.doe")
                .specialization("Crossfit")
                .build();
    }

    public static Trainer constructTrainer() {
        return Trainer.builder()
                .id(2L)
                .specialization(TrainingType.builder().name("Yoga").build())
                .user(User.builder().id(10L).build())
                .build();
    }

    public static Trainer constructTrainer(Long id) {
        return constructTrainer().toBuilder()
                .id(id)
                .user(User.builder().id(id).build())
                .build();
    }

    public static Trainer constructInactiveTrainer() {
        return Trainer.builder()
                .id(2L)
                .specialization(TrainingType.builder().name("Pilates").build())
                .user(User.builder().id(2L).build())
                .build();
    }

    public static AssignedTrainerDTO createAssignedTrainerDTO() {
        return AssignedTrainerDTO.builder()
                .firstName("john")
                .lastName("doe")
                .username("john.doe")
                .specialization("Yoga")
                .build();
    }

    public static AssignedTrainerDTO createAssignedTrainerDTO(String trainerFirstName) {
        return createAssignedTrainerDTO().toBuilder().firstName(trainerFirstName).build();
    }

    public static AssignedTrainerResponse createAssignedTrainerResponse() {
        AssignedTrainerResponse response = new AssignedTrainerResponse();
        response.setFirstName("john");
        response.setLastName("doe");
        response.setUsername("john.doe");
        response.setSpecialization("Yoga");
        return response;
    }

    public static AssignedTrainerResponse createAssignedTrainerResponse(String trainerFirstName) {
        return createAssignedTrainerResponse().firstName(trainerFirstName);
    }

    public static TraineeCreateDTO createTraineeCreateDTO() {
        return TraineeCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Kyiv, Shevchenka 1")
                .build();
    }

    public static TraineeUpdateRequestDTO createTraineeUpdateRequestDTO() {
        return TraineeUpdateRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Kyiv, Khreschatyk 10")
                .build();
    }

    public static Trainee constructTrainee() {
        return Trainee.builder()
                .id(1L)
                .user(User.builder().id(1L).build())
                .address("Kyiv, Shevchenka 1")
                .trainers(Set.of(constructTrainer(2L), constructTrainer(3L)))
                .build();
    }

    public static UserCredentialsDTO constructUserCreateDTO() {
        return UserCredentialsDTO.builder()
                .username("john.doe")
                .password("password")
                .build();
    }

    public static AssignedTraineeDTO createAssignedTraineeDTO() {
        return AssignedTraineeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .build();
    }

    public static TrainingCreateRequest createTrainingCreateRequest() {
        return TrainingCreateRequest.builder()
                .trainerId(1L)
                .traineeId(1L)
                .trainingTypeId(1L)
                .date(LocalDate.of(2025, 7, 4))
                .duration(60L)
                .name("Cardio Session")
                .build();
    }

    public static Training constructTrainingWithoutId() {
        return Training.builder()
                .date(LocalDate.of(2025, 7, 4))
                .duration(60L)
                .name("Cardio Session")
                .build();
    }

    public static Training constructTraining() {
        return Training.builder()
                .id(1L)
                .date(LocalDate.of(2025, 7, 4))
                .duration(60L)
                .name("Cardio Session")
                .build();
    }

    public static TrainingDTO constructTrainingResponse() {
        return TrainingDTO.builder()
                .id(1L)
                .trainerId(1L)
                .traineeId(1L)
                .trainingTypeId(1L)
                .date(LocalDate.of(2025, 7, 4))
                .duration(60L)
                .name("Cardio Session")
                .build();
    }

    public static UserCreateDTO createUserCreateRequest() {
        return UserCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    public static User constructUser() {
        return User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john_doe")
                .password("securePass123")
                .isActive(true)
                .build();
    }

    public static TrainingType constructTrainingType() {
        return TrainingType.builder()
                .id(1L)
                .name("Strength")
                .build();
    }

    public static TrainingTraineeCriteriaFilter buildTraineeCriteriaFilter() {
        return TrainingTraineeCriteriaFilter.builder()
                .traineeId(1L)
                .fromDate(LocalDate.of(2025, 7, 1))
                .toDate(LocalDate.of(2025, 7, 31))
                .trainerName("trainer.one")
                .trainingTypeName("Yoga")
                .build();
    }

    public static TrainingTrainerCriteriaFilter buildTrainerCriteriaFilter() {
        return TrainingTrainerCriteriaFilter.builder()
                .trainerId(1L)
                .fromDate(LocalDate.of(2025, 7, 1))
                .toDate(LocalDate.of(2025, 7, 31))
                .traineeName("john.doe")
                .build();
    }

    public static TraineeUpdateRequest createTraineeUpdateRequest() {
        TraineeUpdateRequest request = new TraineeUpdateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setIsActive(true);
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setAddress("Kyiv, Khreschatyk 10");

        return request;
    }

    public static TraineeUpdateResponse createTraineeUpdateRestResponse() {
        TraineeUpdateResponse response = new TraineeUpdateResponse();
        response.setFirstName("FirstName");
        response.setLastName("LastName");
        response.setUsername("username");
        response.setIsActive(true);
        response.setTrainers(List.of(createAssignedTrainerResponse()));
        response.setAddress("Address");
        response.setDateOfBirth(LocalDate.of(1990, 1, 1));

        return response;
    }

    public static TraineeGetDTO createTraineeGetDTO() {
        return TraineeGetDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .address("Kyiv, Khreschatyk 10")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .trainers(List.of(createAssignedTrainerDTO()))
                .build();
    }

    public static AssignedTrainerResponse createTrainerResponse(String trainerFirstName) {
        return createAssignedTrainerResponse().firstName(trainerFirstName);
    }

    public static TraineeAssignedTrainersUpdateRequest createTraineeAssignedTrainersUpdateRequest() {
        TraineeAssignedTrainersUpdateRequest request = new TraineeAssignedTrainersUpdateRequest();
        request.setTrainerUsernames(List.of("trainer1", "trainer2"));

        return request;
    }

    public static TraineeAssignedTrainersUpdateResponse createTraineeAssignedTrainersUpdateResponse() {
        TraineeAssignedTrainersUpdateResponse response = new TraineeAssignedTrainersUpdateResponse();
        AssignedTrainerResponse rest1 = GymTestProvider.createAssignedTrainerResponse("t1");
        AssignedTrainerResponse rest2 = GymTestProvider.createAssignedTrainerResponse("t2");
        response.setTrainers(List.of(rest1, rest2));

        return response;
    }
}
