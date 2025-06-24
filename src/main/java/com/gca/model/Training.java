package com.gca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private Long id;
    private Long trainerId;
    private Long traineeId;
    private Date trainingDate;
    private Duration trainingDuration;
    private String trainingName;
    private TrainingType trainingType;
}
