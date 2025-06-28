package com.gca.dto.training;

import com.gca.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingResponse {
    private Long id;
    private Long trainerId;
    private Long traineeId;
    private LocalDate trainingDate;
    private Duration trainingDuration;
    private String trainingName;
    private TrainingType trainingType;
}


