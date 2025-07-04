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
import com.gca.model.User;

import java.time.Duration;
import java.time.LocalDate;

public class GymTestProvider {

    public static final Long TRAINEE_USER_ID = 1L;
    public static final String TRAINEE_USERNAME = "john.doe";
    public static final String TRAINEE_FIRSTNAME = "John";
    public static final String TRAINEE_LASTNAME = "Doe";
    public static final String TRAINEE_PASSWORD = "pass123";
    public static final boolean TRAINEE_IS_ACTIVE = true;
    public static final LocalDate TRAINEE_BIRTHDAY = LocalDate.of(2000, 1, 1);
    public static final String TRAINEE_ADDRESS = "address";

    public static final Long TRAINER_USER_ID = 2L;
    public static final String TRAINER_USERNAME = "anna.ivanova";
    public static final String TRAINER_FIRSTNAME = "Anna";
    public static final String TRAINER_LASTNAME = "Ivanova";
    public static final String TRAINER_PASSWORD = "pass123";
    public static final boolean TRAINER_IS_ACTIVE = true;

    public static final TrainingType TRAINER_SPECIALIZATION =
            TrainingType.builder().name("Yoga").build();
    public static final TrainingType TRAINER_SPECIALIZATION_UPDATED =
            TrainingType.builder().name("Pilates").build();;

    public static final Long CARDIO_TYPE_ID = 10L;
    public static final String CARDIO_TYPE_NAME = "Cardio";
    public static final Long STRENGTH_TYPE_ID = 11L;
    public static final String STRENGTH_TYPE_NAME = "Strength";

    public static final Long TRAINING_ID = 1L;
    public static final Long TRAINING_TRAINER_ID = 10L;
    public static final Long TRAINING_TRAINEE_ID = 20L;
    public static final LocalDate TRAINING_DATE = LocalDate.of(2025, 6, 29);
    public static final Long TRAINING_DURATION = 60L;
    public static final String TRAINING_NAME = "Morning Cardio";

    public static final Long STRENGTH_TRAINING_ID = 2L;
    public static final Long STRENGTH_TRAINER_ID = 11L;
    public static final Long STRENGTH_TRAINEE_ID = 21L;
    public static final LocalDate STRENGTH_TRAINING_DATE = LocalDate.of(2025, 7, 1);
    public static final Long STRENGTH_TRAINING_DURATION = 20L;
    public static final String STRENGTH_TRAINING_NAME = "Evening Strength";

    public static User buildTraineeUser() {
        return User.builder()
                .id(TRAINEE_USER_ID)
                .username(TRAINEE_USERNAME)
                .firstName(TRAINEE_FIRSTNAME)
                .lastName(TRAINEE_LASTNAME)
                .password(TRAINEE_PASSWORD)
                .isActive(TRAINEE_IS_ACTIVE)
                .build();
    }

    public static User buildTrainerUser() {
        return User.builder()
                .id(TRAINER_USER_ID)
                .username(TRAINER_USERNAME)
                .firstName(TRAINER_FIRSTNAME)
                .lastName(TRAINER_LASTNAME)
                .password(TRAINER_PASSWORD)
                .isActive(TRAINER_IS_ACTIVE)
                .build();
    }

    // TrainingType helpers
    public static TrainingType constructCardioTrainingType() {
        return TrainingType.builder()
                .id(CARDIO_TYPE_ID)
                .name(CARDIO_TYPE_NAME)
                .build();
    }

    public static TrainingType constructStrengthTrainingType() {
        return TrainingType.builder()
                .id(STRENGTH_TYPE_ID)
                .name(STRENGTH_TYPE_NAME)
                .build();
    }

    // Trainee
    public static TraineeCreateRequest createTraineeCreateRequest() {
        return TraineeCreateRequest.builder()
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(TRAINEE_ADDRESS)
                .build();
    }

    public static Trainee constructTrainee() {
        return Trainee.builder()
                .id(TRAINEE_USER_ID)
                .user(buildTraineeUser())
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(TRAINEE_ADDRESS)
                .build();
    }

    public static TraineeResponse constructTraineeResponse() {
        return TraineeResponse.builder()
                .id(TRAINEE_USER_ID)
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(TRAINEE_ADDRESS)
                .build();
    }

    public static TraineeUpdateRequest createTraineeUpdateRequest() {
        return TraineeUpdateRequest.builder()
                .userId(TRAINEE_USER_ID)
                .dateOfBirth(TRAINEE_BIRTHDAY)
                .address(TRAINEE_ADDRESS)
                .build();
    }

    // Trainer
    public static TrainerCreateRequest createTrainerCreateRequest() {
        return TrainerCreateRequest.builder()
                .specialization(TRAINER_SPECIALIZATION)
                .build();
    }

    public static Trainer constructTrainer() {
        return Trainer.builder()
                .id(TRAINER_USER_ID)
                .user(buildTrainerUser())
                .specialization(TRAINER_SPECIALIZATION)
                .build();
    }

    public static Trainer constructInactiveTrainer() {
        return Trainer.builder()
                .id(TRAINER_USER_ID)
                .user(buildTrainerUser().toBuilder().isActive(false).build())
                .specialization(TRAINER_SPECIALIZATION)
                .build();
    }

    public static Trainer constructUpdatedTrainer() {
        return Trainer.builder()
                .id(TRAINER_USER_ID)
                .user(buildTrainerUser())
                .specialization(TRAINER_SPECIALIZATION_UPDATED)
                .build();
    }

    public static TrainerResponse constructTrainerResponse() {
        return TrainerResponse.builder()
                .id(TRAINER_USER_ID)
                .userId(TRAINER_USER_ID)
                .specialization(TRAINER_SPECIALIZATION)
                .build();
    }

    public static TrainerResponse constructUpdatedTrainerResponse() {
        return TrainerResponse.builder()
                .id(TRAINER_USER_ID)
                .userId(TRAINER_USER_ID)
                .specialization(TRAINER_SPECIALIZATION_UPDATED)
                .build();
    }

    public static TrainerUpdateRequest createTrainerUpdateRequest() {
        return TrainerUpdateRequest.builder()
                .id(TRAINER_USER_ID)
                .specialization(TRAINER_SPECIALIZATION_UPDATED)
                .build();
    }

    public static TrainingCreateRequest createTrainingCreateRequest() {
        return TrainingCreateRequest.builder()
                .trainerId(TRAINING_TRAINER_ID)
                .traineeId(TRAINING_TRAINEE_ID)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(TRAINING_DURATION)
                .trainingName(TRAINING_NAME)
                .build();
    }

    public static Training constructTraining() {
        return Training.builder()
                .id(TRAINING_ID)
                .duration(TRAINING_DURATION)
                .name(TRAINING_NAME)
                .build();
    }

    public static TrainingResponse constructTrainingResponse() {
        return TrainingResponse.builder()
                .id(TRAINING_ID)
                .trainerId(TRAINING_TRAINER_ID)
                .traineeId(TRAINING_TRAINEE_ID)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(TRAINING_DURATION)
                .trainingName(TRAINING_NAME)
                .trainingType(constructCardioTrainingType())
                .build();
    }

    public static TrainingResponse constructStrengthTrainingResponse() {
        return TrainingResponse.builder()
                .id(STRENGTH_TRAINING_ID)
                .trainerId(STRENGTH_TRAINER_ID)
                .traineeId(STRENGTH_TRAINEE_ID)
                .trainingDate(STRENGTH_TRAINING_DATE)
                .trainingDuration(STRENGTH_TRAINING_DURATION)
                .trainingName(STRENGTH_TRAINING_NAME)
                .trainingType(constructStrengthTrainingType())
                .build();
    }
}
