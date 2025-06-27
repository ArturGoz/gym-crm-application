package com.gca.facade;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.service.TraineeService;
import com.gca.service.TrainerService;
import com.gca.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingAppFacade {
    private static final Logger logger = LoggerFactory.getLogger(TrainingAppFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public Trainee createTrainee(Trainee trainee) {
        logger.info("Facade: Creating trainee {}", trainee.getFirstName() + " " + trainee.getLastName());

        return traineeService.createTrainee(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Facade: Updating trainee with ID {}", trainee.getUserId());

        return traineeService.updateTrainee(trainee);
    }

    public void deleteTrainee(Long id) {
        logger.info("Facade: Deleting trainee with ID {}", id);

        traineeService.deleteTrainee(id);
    }

    public Trainee getTraineeById(Long id) {
        logger.info("Facade: Retrieving trainee with ID {}", id);

        return traineeService.getTraineeById(id);
    }

    public List<Trainee> getAllTrainees() {
        logger.info("Facade: Retrieving all trainees");

        return traineeService.getAllTrainees();
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Facade: Creating trainer {}", trainer.getFirstName() + " " + trainer.getLastName());

        return trainerService.createTrainer(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Facade: Updating trainer with ID {}", trainer.getUserId());

        return trainerService.updateTrainer(trainer);
    }

    public Trainer getTrainerById(Long id) {
        logger.info("Facade: Retrieving trainer with ID {}", id);

        return trainerService.getTrainerById(id);
    }

    public List<Trainer> getAllTrainers() {
        logger.info("Facade: Retrieving all trainers");

        return trainerService.getAllTrainers();
    }

    public Training createTraining(Training training) {
        logger.info("Facade: Creating training with trainerId={}, traineeId={}",
                training.getTrainerId(), training.getTraineeId());

        return trainingService.createTraining(training);
    }

    public Training getTrainingById(Long id) {
        logger.info("Facade: Retrieving training with ID {}", id);

        return trainingService.getTrainingById(id);
    }

    public List<Training> getAllTrainings() {
        logger.info("Facade: Retrieving all trainings");

        return trainingService.getAllTrainings();
    }
}
