package provider;

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

    public static TraineeCreateRequest traineeCreateRequest() {
        return TraineeCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();
    }

    public static Trainee trainee() {
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

    public static TraineeResponse traineeResponse() {
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

    public static TraineeUpdateRequest traineeUpdateRequest() {
        return TraineeUpdateRequest.builder()
                .userId(1L)
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();
    }

    public static TrainerCreateRequest trainerCreateRequest() {
        return TrainerCreateRequest.builder()
                .firstName("Anna")
                .lastName("Ivanova")
                .specialization("Yoga")
                .build();
    }

    public static Trainer trainer() {
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

    public static Trainer trainerInactive() {
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

    public static Trainer trainerUpdated() {
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

    public static TrainerResponse trainerResponse() {
        return TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Yoga")
                .build();
    }

    public static TrainerResponse trainerResponseUpdated() {
        return TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Pilates")
                .build();
    }

    public static TrainerUpdateRequest trainerUpdateRequest() {
        return TrainerUpdateRequest.builder()
                .userId(2L)
                .isActive(true)
                .specialization("Pilates")
                .build();
    }

    public static TrainerUpdateRequest trainerUpdateRequestNotFound() {
        return TrainerUpdateRequest.builder()
                .userId(3L)
                .isActive(true)
                .specialization("Crossfit")
                .build();
    }

    public static TrainingType trainingTypeCardio() {
        return TrainingType.builder()
                .name("Cardio")
                .build();
    }

    public static TrainingType trainingTypeStrength() {
        return TrainingType.builder()
                .name("Strength")
                .build();
    }

    public static TrainingCreateRequest trainingCreateRequest() {
        return TrainingCreateRequest.builder()
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(trainingTypeCardio())
                .build();
    }

    public static Training training() {
        return Training.builder()
                .id(1L)
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(trainingTypeCardio())
                .build();
    }

    public static Training trainingWithoutId() {
        return Training.builder()
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(trainingTypeCardio())
                .build();
    }

    public static TrainingResponse trainingResponse() {
        return TrainingResponse.builder()
                .id(1L)
                .trainerId(10L)
                .traineeId(20L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofMinutes(60))
                .trainingName("Morning Cardio")
                .trainingType(trainingTypeCardio())
                .build();
    }

    public static Training trainingStrength() {
        return Training.builder()
                .id(2L)
                .trainerId(11L)
                .traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(45))
                .trainingName("Evening Strength")
                .trainingType(trainingTypeStrength())
                .build();
    }

    public static TrainingResponse trainingResponseStrength() {
        return TrainingResponse.builder()
                .id(2L)
                .trainerId(11L)
                .traineeId(21L)
                .trainingDate(LocalDate.of(2025, 7, 1))
                .trainingDuration(Duration.ofMinutes(45))
                .trainingName("Evening Strength")
                .trainingType(trainingTypeStrength())
                .build();
    }
}
