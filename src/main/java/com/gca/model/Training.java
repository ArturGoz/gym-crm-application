package com.gca.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@SuperBuilder(toBuilder = true)
public class Training {
    private final Long id;
    private final Long trainerId;
    private final Long traineeId;
    private final LocalDate trainingDate;
    private final Duration trainingDuration;
    private final String trainingName;
    private final TrainingType trainingType;
}


