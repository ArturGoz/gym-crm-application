package com.gca.facade;

import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.security.Authenticated;
import com.gca.service.TraineeService;
import com.gca.service.TrainerService;
import com.gca.service.TrainingService;
import com.gca.service.UserService;
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
    private final UserService userService;

    public TraineeResponse createTrainee(TraineeCreateRequest request) {
        logger.info("Facade: Creating trainee {} ", request.getUserId());
        return traineeService.createTrainee(request);
    }

    @Authenticated
    public TraineeResponse updateTrainee(TraineeUpdateRequest request) {
        logger.info("Facade: Updating trainee with ID {}", request.getUserId());
        return traineeService.updateTrainee(request);
    }

    @Authenticated
    public void deleteTrainee(Long id) {
        logger.info("Facade: Deleting trainee with ID {}", id);
        traineeService.deleteTraineeById(id);
    }

    @Authenticated
    public TraineeResponse getTraineeById(Long id) {
        logger.info("Facade: Retrieving trainee with ID {}", id);
        return traineeService.getTraineeById(id);
    }

    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        logger.info("Facade: Creating trainer {}", request.getUserId());
        return trainerService.createTrainer(request);
    }

    @Authenticated
    public TrainerResponse updateTrainer(TrainerUpdateRequest request) {
        logger.info("Facade: Updating trainer with ID {}", request.getId());
        return trainerService.updateTrainer(request);
    }

    @Authenticated
    public TrainerResponse getTrainerById(Long id) {
        logger.info("Facade: Retrieving trainer with ID {}", id);
        return trainerService.getTrainerById(id);
    }

    @Authenticated
    public TrainingResponse createTraining(TrainingCreateRequest request) {
        logger.info("Facade: Creating training with trainerId={}, traineeId={}",
                request.getTrainerId(), request.getTraineeId());
        return trainingService.createTraining(request);
    }

    @Authenticated
    public TrainingResponse getTrainingById(Long id) {
        logger.info("Facade: Retrieving training with ID {}", id);
        return trainingService.getTrainingById(id);
    }

    @Authenticated
    public void changePassword(PasswordChangeRequest request) {
        logger.info("Changing password for userId: {}", request.getUserId());

        userService.changeUserPassword(request);
    }

    @Authenticated
    public List<TrainingResponse> findFilteredTrainings(TrainingTrainerCriteriaFilter filter) {
        logger.info("Facade: Retrieving trainings with filter {}", filter);

        return trainingService.getTrainerTrainings(filter);
    }

    @Authenticated
    public List<TrainingResponse> findFilteredTrainings(TrainingTraineeCriteriaFilter filter) {
        logger.info("Facade: Retrieving trainees with filter {}", filter);

        return trainingService.getTraineeTrainings(filter);
    }

    @Authenticated
    public TraineeResponse getTraineeByUsername(String username) {
        logger.info("Facade: Retrieving traine by name {}", username);

        return traineeService.getTraineeByUsername(username);
    }

    @Authenticated
    public TrainerResponse getTrainerByUsername(String username) {
        logger.info("Facade: Retrieving trainer by name {}", username);

        return trainerService.getTrainerByUsername(username);
    }
}
