package com.gca.model;

import lombok.Data;

import java.time.Duration;
import java.util.Date;

@Data
public class Training {
    private Long trainerId;
    private Long traineeId;
    private Date trainingDate;
    private Duration trainingDuration;
    private String trainingName;
    private TrainingType trainingType;
}
