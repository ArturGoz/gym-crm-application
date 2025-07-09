package com.gca;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserResponse;
import com.gca.dto.user.UserUpdateRequest;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;

import java.time.LocalDate;

public class GymTestProvider {

    public static TrainerCreateRequest createTrainerCreateRequest() {
        return TrainerCreateRequest.builder()
                .userId(10L)
                .specialization(TrainingType.builder().name("Yoga").build())
                .build();
    }

    public static TrainerUpdateRequest createTrainerUpdateRequest() {
        return TrainerUpdateRequest.builder()
                .id(2L)
                .specialization(TrainingType.builder().name("Pilates").build())
                .build();
    }

    public static TrainerUpdateRequest createTrainerUpdateRequestNotFound() {
        return TrainerUpdateRequest.builder()
                .id(3L)
                .specialization(TrainingType.builder().name("Boxing").build())
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
                .id(2L)
                .userId(10L)
                .specialization(TrainingType.builder().name("Yoga").build())
                .build();
    }

    public static TrainerResponse constructUpdatedTrainerResponse() {
        return TrainerResponse.builder()
                .id(2L)
                .userId(2L)
                .specialization(TrainingType.builder().name("Pilates").build())
                .build();
    }

    public static TraineeCreateRequest createTraineeCreateRequest() {
        return TraineeCreateRequest.builder()
                .userId(1L)
                .address("Kyiv, Shevchenka 1")
                .build();
    }

    public static TraineeUpdateRequest createTraineeUpdateRequest() {
        return TraineeUpdateRequest.builder()
                .id(1L)
                .userId(1L)
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

    public static TraineeResponse constructTraineeResponse() {
        return TraineeResponse.builder()
                .id(1L)
                .userId(1L)
                .address("Kyiv, Shevchenka 1")
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

    public static Training constructStrengthTraining() {
        return Training.builder()
                .id(2L)
                .date(LocalDate.of(2025, 7, 5))
                .duration(90L)
                .name("Strength Training")
                .build();
    }

    public static TrainingResponse constructStrengthTrainingResponse() {
        return TrainingResponse.builder()
                .id(2L)
                .trainerId(2L)
                .traineeId(2L)
                .trainingTypeId(2L)
                .date(LocalDate.of(2025, 7, 5))
                .duration(90L)
                .name("Strength Training")
                .build();
    }

    public static UserCreateRequest createUserCreateRequest() {
        return UserCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    public static UserUpdateRequest createUserUpdateRequest() {
        return UserUpdateRequest.builder()
                .id(1L)
                .firstName("UpdatedJohn")
                .lastName("UpdatedDoe")
                .username("updated_john_doe")
                .password("updatedPass123")
                .isActive(true)
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

    public static UserResponse constructUserResponse() {
        return UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john_doe")
                .password("securePass123")
                .isActive(true)
                .build();
    }
}
