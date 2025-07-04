package com.gca.dto.training;

import com.gca.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDate;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCreateRequest {
    private Long trainerId;
    private Long traineeId;
    private LocalDate trainingDate;
    private Long trainingDuration;
    private String trainingName;
    private TrainingType trainingType;
}
