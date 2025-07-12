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
public class TrainingTrainerCriteriaFilter {

    @NotNull(message = "Trainer ID must be provided")
    @Positive(message = "Trainer ID must be positive")
    private Long trainerId;

    private LocalDate fromDate;
    private LocalDate toDate;

    @Size(max = 50, message = "Trainee name must not exceed 50 characters")
    private String traineeName;
}
