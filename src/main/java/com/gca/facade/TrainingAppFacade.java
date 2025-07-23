package com.gca.facade;

import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeTrainersUpdateDTO;
import com.gca.dto.trainee.TraineeUpdateRequestDTO;
import com.gca.dto.trainee.TraineeUpdateResponseDTO;
import com.gca.dto.trainer.TrainerCreateDTO;
import com.gca.dto.trainer.TrainerUpdateRequestDTO;
import com.gca.dto.trainer.TrainerUpdateResponseDTO;
import com.gca.dto.training.TrainingCreateDTO;
import com.gca.dto.training.TrainingDTO;
import com.gca.dto.user.UserCredentialsDTO;
import com.gca.mapper.rest.RestTraineeMapper;
import com.gca.mapper.rest.RestTrainerMapper;
import com.gca.mapper.rest.RestTrainingMapper;
import com.gca.model.TrainingType;
import com.gca.openapi.model.AssignedTrainerResponse;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateRequest;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateResponse;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
import com.gca.openapi.model.TraineeGetResponse;
import com.gca.openapi.model.TraineeUpdateRequest;
import com.gca.openapi.model.TraineeUpdateResponse;
import com.gca.openapi.model.TrainerCreateRequest;
import com.gca.openapi.model.TrainerCreateResponse;
import com.gca.openapi.model.TrainerGetResponse;
import com.gca.openapi.model.TrainerUpdateRequest;
import com.gca.openapi.model.TrainerUpdateResponse;
import com.gca.openapi.model.TrainingCreateRequest;
import com.gca.openapi.model.TrainingGetResponse;
import com.gca.openapi.model.TrainingTypeResponse;
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

    private final RestTraineeMapper restTraineeMapper;
    private final RestTrainerMapper restTrainerMapper;
    private final RestTrainingMapper restTrainingMapper;

    public TraineeCreateResponse createTrainee(TraineeCreateRequest request) {
        logger.info("Facade: Creating trainee {} ", request.getFirstName());

        TraineeCreateDTO traineeCreateDTO = restTraineeMapper.toDto(request);
        UserCredentialsDTO userCredentialsDTO = traineeService.createTrainee(traineeCreateDTO);

        return restTraineeMapper.toRest(userCredentialsDTO);
    }

    @Authenticated
    public TraineeUpdateResponse updateTrainee(String username, TraineeUpdateRequest request) {
        logger.info("Facade: Updating trainee with request {}", request);

        TraineeUpdateRequestDTO requestDTO = restTraineeMapper.toDto(username, request);
        TraineeUpdateResponseDTO responseDTO = traineeService.updateTrainee(requestDTO);

        return restTraineeMapper.toRest(responseDTO);
    }

    public TrainerCreateResponse createTrainer(TrainerCreateRequest request) {
        logger.info("Facade: Creating trainer {}", request.getFirstName());

        TrainerCreateDTO createDTO = restTrainerMapper.toDto(request);
        UserCredentialsDTO credentialsDTO = trainerService.createTrainer(createDTO);

        return restTrainerMapper.toRest(credentialsDTO);
    }

    @Authenticated
    public TrainerUpdateResponse updateTrainer(String username, TrainerUpdateRequest trainer) {
        logger.info("Facade: Updating trainer with username {}", username);

        TrainerUpdateRequestDTO requestDTO = restTrainerMapper.toDto(username, trainer);
        TrainerUpdateResponseDTO responseDTO = trainerService.updateTrainer(requestDTO);

        return restTrainerMapper.toRest(responseDTO);
    }

    @Authenticated
    public void createTraining(TrainingCreateRequest request) {
        logger.info("Facade: Creating training with trainerId={}, traineeId={} and training name={}",
                request.getTrainerUsername(), request.getTraineeUsername(), request.getTrainingName());

        TrainingCreateDTO dto = restTrainingMapper.toDto(request);
        trainingService.createTraining(dto);
    }

    @Authenticated
    public void changePassword(PasswordChangeRequest request) {
        logger.info("Changing password for userId: {}", request.getUserId());

        userService.changeUserPassword(request);
    }

    @Authenticated
    public List<TrainingGetResponse> findFilteredTrainings(TrainingTrainerCriteriaFilter filter) {
        logger.info("Facade: Retrieving trainings with filter {}", filter);

        List<TrainingDTO> dtoList = trainingService.getTrainerTrainings(filter);

        return dtoList.stream()
                .map(restTrainingMapper::toRest)
                .toList();
    }

    @Authenticated
    public List<TrainingGetResponse> findFilteredTrainings(TrainingTraineeCriteriaFilter filter) {
        logger.info("Facade: Retrieving trainees with filter {}", filter);

        List<TrainingDTO> dtoList = trainingService.getTraineeTrainings(filter);

        return dtoList.stream()
                .map(restTrainingMapper::toRest)
                .toList();
    }

    @Authenticated
    public TraineeGetResponse getTraineeByUsername(String username) {
        logger.info("Facade: Retrieving traine by name {}", username);

        return restTraineeMapper.toRest(traineeService.getTraineeByUsername(username));
    }

    @Authenticated
    public TrainerGetResponse getTrainerByUsername(String username) {
        logger.info("Facade: Retrieving trainer by name {}", username);

        return restTrainerMapper.toRest(trainerService.getTrainerByUsername(username));
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
    public List<AssignedTrainerResponse> getUnassignedTrainers(String traineeUsername) {
        logger.info("Facade: Retrieving unassigned trainers");

        return trainerService.getUnassignedTrainers(traineeUsername).stream()
                .map(restTrainerMapper::toRest)
                .toList();
    }

    @Authenticated
    public TraineeAssignedTrainersUpdateResponse updateTraineeTrainers(String username,
                                                                       TraineeAssignedTrainersUpdateRequest request) {
        logger.info("Updating trainee list of trainers");

        TraineeTrainersUpdateDTO traineeTrainersRequest
                = new TraineeTrainersUpdateDTO(username, request.getTrainerUsernames());

        List<AssignedTrainerResponse> trainerList =
                traineeService.updateTraineeTrainers(traineeTrainersRequest).stream()
                        .map(restTrainerMapper::toRest)
                        .toList();

        TraineeAssignedTrainersUpdateResponse response = new TraineeAssignedTrainersUpdateResponse();
        response.setTrainers(trainerList);

        return response;
    }

    @Authenticated
    public List<TrainingTypeResponse> getAllTrainingTypes() {
        logger.info("Facade: Retrieving all training types");

        List<TrainingType> typeList = trainingService.getAllTrainingTypes();

        return typeList.stream()
                .map(restTrainingMapper::toRest)
                .toList();
    }
}


