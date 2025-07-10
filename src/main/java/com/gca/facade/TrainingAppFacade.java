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

@Service
@RequiredArgsConstructor
public class TrainingAppFacade {
    private static final Logger logger = LoggerFactory.getLogger(TrainingAppFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public TraineeResponse createTrainee(TraineeCreateRequest request) {
        logger.info("Facade: Creating trainee {} ", request.getUserId());
        return traineeService.createTrainee(request);
    }

    public TraineeResponse updateTrainee(TraineeUpdateRequest request) {
        logger.info("Facade: Updating trainee with ID {}", request.getUserId());
        return traineeService.updateTrainee(request);
    }

    public void deleteTrainee(Long id) {
        logger.info("Facade: Deleting trainee with ID {}", id);
        traineeService.deleteTraineeById(id);
    }

    public TraineeResponse getTraineeById(Long id) {
        logger.info("Facade: Retrieving trainee with ID {}", id);
        return traineeService.getTraineeById(id);
    }

    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        logger.info("Facade: Creating trainer {}", request.getUserId());
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

    public TrainingResponse createTraining(TrainingCreateRequest request) {
        logger.info("Facade: Creating training with trainerId={}, traineeId={}",
                request.getTrainerId(), request.getTraineeId());
        return trainingService.createTraining(request);
    }

    public TrainingResponse getTrainingById(Long id) {
        logger.info("Facade: Retrieving training with ID {}", id);
        return trainingService.getTrainingById(id);
    }
}
