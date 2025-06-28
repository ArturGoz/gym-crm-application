package com.gca.facade;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
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

    public TraineeResponse createTrainee(TraineeCreateRequest request) {
        logger.info("Facade: Creating trainee {} {}", request.getFirstName(), request.getLastName());
        return traineeService.createTrainee(request);
    }

    public TraineeResponse updateTrainee(TraineeUpdateRequest request) {
        logger.info("Facade: Updating trainee with ID {}", request.getId());
        return traineeService.updateTrainee(request);
    }

    public void deleteTrainee(Long id) {
        logger.info("Facade: Deleting trainee with ID {}", id);
        traineeService.deleteTrainee(id);
    }

    public TraineeResponse getTraineeById(Long id) {
        logger.info("Facade: Retrieving trainee with ID {}", id);
        return traineeService.getTraineeById(id);
    }

    public TraineeResponse getTraineeByUsername(String username) {
        logger.info("Facade: Retrieving trainee with username {}", username);
        return traineeService.getTraineeByUsername(username);
    }

    public List<TraineeResponse> getAllTrainees() {
        logger.info("Facade: Retrieving all trainees");
        return traineeService.getAllTrainees();
    }

    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        logger.info("Facade: Creating trainer {} {}", request.getFirstName(), request.getLastName());
        return trainerService.createTrainer(request);
    }

    public TrainerResponse updateTrainer(TrainerUpdateRequest request) {
        logger.info("Facade: Updating trainer with ID {}", request.getId());
        return trainerService.updateTrainer(request);
    }

    public TrainerResponse getTrainerById(Long id) {
        logger.info("Facade: Retrieving trainer with ID {}", id);
        return trainerService.getTrainerById(id);
    }

    public TrainerResponse getTrainerByUsername(String username) {
        logger.info("Facade: Retrieving trainer with username {}", username);
        return trainerService.getTrainerByUsername(username);
    }

    public List<TrainerResponse> getAllTrainers() {
        logger.info("Facade: Retrieving all trainers");
        return trainerService.getAllTrainers();
    }

    public TrainingResponse createTraining(TrainingCreateRequest request) {
        logger.info("Facade: Creating training with trainerId={}, traineeId={}",
                request.getTrainerId(), request.getTraineeId());
        return trainingService.createTraining(request);
    }

    public TrainingResponse getTrainingById(Long id) {
        logger.info("Facade: Retrieving training with ID {}", id);
        return trainingService.getTrainingById(id);
    }

    public List<TrainingResponse> getAllTrainings() {
        logger.info("Facade: Retrieving all trainings");
        return trainingService.getAllTrainings();
    }
}
