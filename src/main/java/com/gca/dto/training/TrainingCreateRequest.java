package com.gca.dto.training;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCreateRequest {
    @NotNull(message = "Trainer ID cannot be null")
    private Long trainerId;

    @NotNull(message = "Trainee ID cannot be null")
    private Long traineeId;

    @NotNull(message = "Training type ID cannot be null")
    private Long trainingTypeId;

    @NotNull(message = "Date cannot be null")
    @FutureOrPresent(message = "Training date must be in future or present")
    private LocalDate date;

    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration must be positive")
    private Long duration;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 100, message = "Name must be 1-100 characters")
    private String name;
}
