package com.gca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private Long id;
    private Long trainerId;
    private Long traineeId;
    private LocalDate trainingDate;
    private Duration trainingDuration;
    private String trainingName;
    private TrainingType trainingType;
}


