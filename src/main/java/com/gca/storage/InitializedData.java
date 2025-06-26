package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class InitializedData {
    private Set<Training> trainings = new HashSet<>();
    private Set<Trainer> trainers = new HashSet<>();
    private Set<Trainee> trainees = new HashSet<>();
}
