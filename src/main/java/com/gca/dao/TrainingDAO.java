package com.gca.dao;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingDAO {
    Training create(Training training);

    Training getById(Long id);

    public List<Training> getTraineeTrainings(Trainee trainee, LocalDate fromDate,
                                              LocalDate toDate, String trainerName,
                                              String trainingTypeName);

    public List<Training> getTrainerTrainings(Trainer trainer, LocalDate fromDate,
                                              LocalDate toDate, String traineeName);
}
