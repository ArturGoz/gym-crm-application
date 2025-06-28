package com.gca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDate;

@Data
@SuperBuilder(toBuilder = true)
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


