package com.gca.repository;

import com.gca.repository.criteria.TrainingSpecifications;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {

    default List<Training> getTrainerTrainings(Trainer trainer, LocalDate fromDate, LocalDate toDate, String traineeName) {
        return findAll(TrainingSpecifications.trainerTrainings(trainer, fromDate, toDate, traineeName));
    }

    default List<Training> getTraineeTrainings(Trainee trainee, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingTypeName) {
        return findAll(TrainingSpecifications.traineeTrainings(trainee, fromDate, toDate, trainerName, trainingTypeName));
    }
}

