package com.gca.facade;

import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateResponse;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateResponse;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.dto.user.UserCreationResponse;
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

    public UserCreationResponse createTrainee(TraineeCreateRequest request) {
        logger.info("Facade: Creating trainee {} ", request.getFirstName());
        return traineeService.createTrainee(request);
    }

    @Authenticated
    public TraineeUpdateResponse updateTrainee(TraineeUpdateData request) {
        logger.info("Facade: Updating trainee with request {}", request);
        return traineeService.updateTrainee(request);
    }

    public UserCreationResponse createTrainer(TrainerCreateRequest request) {
        logger.info("Facade: Creating trainer {}", request.getFirstName());
        return trainerService.createTrainer(request);
    }

    @Authenticated
    public TrainerUpdateResponse updateTrainer(TrainerUpdateRequest request) {
        logger.info("Facade: Updating trainer with username {}", request.getUsername());
        return trainerService.updateTrainer(request);
    }

    @Authenticated
    public TrainingResponse createTraining(TrainingCreateRequest request) {
        logger.info("Facade: Creating training with trainerId={}, traineeId={}",
                request.getTrainerId(), request.getTraineeId());
        return trainingService.createTraining(request);
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

    @Authenticated
    public void deleteTraineeByUsername(String username) {
        logger.info("Facade: Deleting traine by name {}", username);

        traineeService.deleteTraineeByUsername(username);
    }

    @Authenticated
    public void toggleUserActiveStatus(String username) {
        logger.info("Facade: Toggle user active status for user {}", username);

        userService.toggleActiveStatus(username);
    }

    @Authenticated
    public List<TrainerResponse> getUnassignedTrainers(String traineeUsername) {
        logger.info("Facade: Retrieving unassigned trainers");

        return trainerService.getUnassignedTrainers(traineeUsername);
    }

    @Authenticated
    public TraineeResponse updateTraineeTrainers(UpdateTraineeTrainersRequest request) {
        logger.info("Updating trainee list of trainers");

        return traineeService.updateTraineeTrainers(request);
    }
}


