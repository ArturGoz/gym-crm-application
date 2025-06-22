package com.gca.model;

import lombok.Data;

import java.util.Date;

@Data
public class Training {
    private Date trainingDate;
    private double trainingDuration;
    private TrainingType trainingType;
    private Trainer trainer;
    private Trainee trainee;
}
