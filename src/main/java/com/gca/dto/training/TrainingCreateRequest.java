package com.gca.dto.training;

import com.gca.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCreateRequest {
    private Long trainerId;
    private Long traineeId;
    private LocalDate trainingDate;
    private Duration trainingDuration;
    private String trainingName;
    private TrainingType trainingType;
}
