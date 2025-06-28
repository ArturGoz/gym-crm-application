package com.gca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Training {
    private final Long id;
    private final Long trainerId;
    private final Long traineeId;
    private final LocalDate trainingDate;
    private final Duration trainingDuration;
    private final String trainingName;
    private final TrainingType trainingType;
}


