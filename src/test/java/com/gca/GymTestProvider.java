package com.gca;

import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateResponse;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateResponse;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserCreationResponse;
import com.gca.dto.user.UserResponse;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;

import java.time.LocalDate;
import java.util.List;

public class GymTestProvider {

    public static UpdateTraineeTrainersRequest createUpdateTraineeTrainersRequest() {
        return UpdateTraineeTrainersRequest.builder()
                .traineeUsername("john_doe")
                .trainerNames(List.of("trainer1", "trainer2"))
                .build();
    }

    public static TrainerUpdateResponse createTrainerUpdateResponse() {
        return TrainerUpdateResponse.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .username("username")
                .isActive(true)
                .trainees(List.of(constructTraineeResponse()))
                .specializationId(1L)
                .build();
    }

    public static TraineeUpdateResponse createTraineeUpdateResponse() {
        return TraineeUpdateResponse.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .username("username")
                .isActive(true)
                .trainers(List.of(constructTrainerResponse()))
                .address("Address")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    public static TrainerCreateRequest createTrainerCreateRequest() {
        return TrainerCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .specializationId(1L)
                .build();
    }

    public static TrainerUpdateRequest createTrainerUpdateRequest() {
        return TrainerUpdateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .username("john.doe")
                .specializationId(1L)
                .build();
    }

    public static Trainer constructTrainer() {
        return Trainer.builder()
                .id(2L)
                .specialization(TrainingType.builder().name("Yoga").build())
                .user(User.builder().id(10L).build())
                .build();
    }

    public static Trainer constructInactiveTrainer() {
        return Trainer.builder()
                .id(2L)
                .specialization(TrainingType.builder().name("Pilates").build())
                .user(User.builder().id(2L).build())
                .build();
    }

    public static Trainer constructUpdatedTrainer() {
        return Trainer.builder()
                .id(2L)
                .specialization(TrainingType.builder().name("Pilates").build())
                .user(User.builder().id(2L).build())
                .build();
    }

    public static TrainerResponse constructTrainerResponse() {
        return TrainerResponse.builder()
                .firstName("john")
                .lastName("doe")
                .username("john.doe")
                .specializationId(1L)
                .build();
    }

    public static TraineeCreateRequest createTraineeCreateRequest() {
        return TraineeCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Kyiv, Shevchenka 1")
                .build();
    }

    public static TraineeUpdateData createTraineeUpdateRequest() {
        return TraineeUpdateData.builder()
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
                .build();
    }

    public static UserCreationResponse constructUserCreationResponse() {
        return UserCreationResponse.builder()
                .username("john.doe")
                .password("password")
                .build();
    }

    public static TraineeResponse constructTraineeResponse() {
        return TraineeResponse.builder()
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

    public static TrainingResponse constructTrainingResponse() {
        return TrainingResponse.builder()
                .id(1L)
                .trainerId(1L)
                .traineeId(1L)
                .trainingTypeId(1L)
                .date(LocalDate.of(2025, 7, 4))
                .duration(60L)
                .name("Cardio Session")
                .build();
    }

    public static UserCreateRequest createUserCreateRequest() {
        return UserCreateRequest.builder()
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
}
