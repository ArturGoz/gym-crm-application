package com.gca.dto.filter;

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
public class TrainingTraineeCriteriaFilter {

    @NotNull(message = "Trainee ID must be provided")
    @Positive(message = "Trainee ID must be positive")
    private Long traineeId;

    private LocalDate fromDate;
    private LocalDate toDate;

    @Size(max = 50, message = "Trainer name must not exceed 50 characters")
    private String trainerName;

    @Size(max = 50, message = "Training type name must not exceed 50 characters")
    private String trainingTypeName;
}
