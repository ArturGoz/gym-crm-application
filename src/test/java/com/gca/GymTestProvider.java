package com.gca;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;

import java.time.Duration;
import java.time.LocalDate;

public class GymTestProvider {

    public static TraineeCreateRequest createTraineeCreateRequest() {
        return TraineeCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();
    }

    public static Trainee constructTrainee() {
        return Trainee.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("pass123")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();
    }

    public static TraineeResponse constructTraineeResponse() {
        return TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();
    }

    public static TraineeUpdateRequest createTraineeUpdateRequest() {
        return TraineeUpdateRequest.builder()
                .userId(1L)
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();
    }

    public static TrainerCreateRequest createTrainerCreateRequest() {
        return TrainerCreateRequest.builder()
                .firstName("Anna")
                .lastName("Ivanova")
                .specialization("Yoga")
                .build();
    }

    public static Trainer constructTrainer() {
        return Trainer.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .password("pass123")
                .isActive(true)
                .specialization("Yoga")
                .build();
    }

    public static Trainer constructInactiveTrainer() {
        return Trainer.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .password("pass123")
                .isActive(false)
                .specialization("Yoga")
                .build();
    }

    public static Trainer constructUpdatedTrainer() {
        return Trainer.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .password("pass123")
                .isActive(true)
                .specialization("Pilates")
                .build();
    }

    public static TrainerResponse constructTrainerResponse() {
        return TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Yoga")
                .build();
    }

    public static TrainerResponse constructUpdatedTrainerResponse() {
        return TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Pilates")
                .build();
    }

    public static TrainerUpdateRequest createTrainerUpdateRequest() {
        return TrainerUpdateRequest.builder()
                .userId(2L)
                .isActive(true)
                .specialization("Pilates")
                .build();
    }

    public static TrainerUpdateRequest createTrainerUpdateRequestNotFound() {
        return TrainerUpdateRequest.builder()
                .userId(3L)
                .isActive(true)
                .specialization("Crossfit")
                .build();
    }

    public static TrainingType constructCardioTrainingType() {
        return TrainingType.builder()
                .name("Cardio")
                .build();
    }

    public static TrainingType constructStrengthTrainingType() {
        return TrainingType.builder()
                .name("Strength")
                .build();
    }

    public static TrainingCreateRequest createTrainingCreateRequest() {
        return TrainingCreateRequest.builder()
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(constructCardioTrainingType())
                .build();
    }

    public static Training constructTraining() {
        return Training.builder()
                .id(1L)
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(constructCardioTrainingType())
                .build();
    }

    public static Training constructTrainingWithoutId() {
        return Training.builder()
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(constructCardioTrainingType())
                .build();
    }

    public static TrainingResponse constructTrainingResponse() {
        return TrainingResponse.builder()
                .id(1L)
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(constructCardioTrainingType())
                .build();
    }

    public static Training constructStrengthTraining() {
        return Training.builder()
                .id(2L)
                .trainerId(11L)
                .traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(45))
                .trainingName("Evening Strength")
                .trainingType(constructStrengthTrainingType())
                .build();
    }

    public static TrainingResponse constructStrengthTrainingResponse() {
        return TrainingResponse.builder()
                .id(2L)
                .trainerId(11L)
                .traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(45))
                .trainingName("Evening Strength")
                .trainingType(constructStrengthTrainingType())
                .build();
    }
}
