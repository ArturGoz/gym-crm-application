package com.gca;

import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerDTO;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingDTO;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserDTO;
import com.gca.dto.user.UserUpdateRequest;
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

    public static TrainerDTO constructTrainerResponse() {
        return TrainerDTO.builder()
                .id(2L)
                .userId(10L)
                .specialization(TrainingType.builder().name("Yoga").build())
                .build();
    }

    public static TrainerDTO constructUpdatedTrainerResponse() {
        return TrainerDTO.builder()
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

    public static TraineeDTO constructTraineeResponse() {
        return TraineeDTO.builder()
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

    public static Training constructStrengthTraining() {
        return Training.builder()
                .id(2L)
                .date(LocalDate.of(2025, 7, 5))
                .duration(90L)
                .name("Strength Training")
                .build();
    }

    public static TrainingDTO constructStrengthTrainingResponse() {
        return TrainingDTO.builder()
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

    public static UserDTO constructUserResponse() {
        return UserDTO.builder()
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
