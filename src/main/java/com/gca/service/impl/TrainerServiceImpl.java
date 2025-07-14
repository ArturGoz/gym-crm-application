package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateResponse;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserCreationResponse;
import com.gca.exception.ServiceException;
import com.gca.mapper.TrainerMapper;
import com.gca.mapper.UserMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import com.gca.service.TrainerService;
import com.gca.service.UserService;
import com.gca.service.common.CoreValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Validated
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerDAO trainerDAO;
    private TraineeDAO traineeDAO;
    private TrainingTypeDAO trainingTypeDAO;

    private UserService userService;

    private UserMapper userMapper;
    private TrainerMapper trainerMapper;

    private CoreValidator validator;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setUserDAO(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTrainerMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    @Autowired
    public void setValidator(CoreValidator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public UserCreationResponse createTrainer(@Valid TrainerCreateRequest request) {
        logger.debug("Creating trainer");

        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        User user = userService.createUser(userCreateRequest);
        TrainingType trainingType = trainingTypeDAO.getById(request.getSpecializationId());

        if (trainingType == null) {
            throw new ServiceException("Invalid training type");
        }

        Trainer trainer = Trainer.builder()
                .specialization(trainingType)
                .user(user)
                .build();

        Trainer created = trainerDAO.create(trainer);

        logger.info("Trainer created: {}", created);
        return userMapper.toResponse(user);
    }

    @Transactional
    @Override
    public TrainerUpdateResponse updateTrainer(@Valid TrainerUpdateRequest request) {
        logger.debug("Updating trainer");

        Trainer trainer = Optional.ofNullable(trainerDAO.findByUsername(request.getUsername()))
                .orElseThrow(() -> new ServiceException(
                        format("Invalid trainer username: %s", request.getUsername())
                ));

        User updatedUser = trainerMapper.fillUserFields(trainer.getUser(), request);

        TrainingType trainingType = Optional.ofNullable(trainingTypeDAO.getById(request.getSpecializationId()))
                .orElse(trainer.getSpecialization());

        Trainer updatedTrainer = trainerMapper.fillTrainerFields(trainer, updatedUser, trainingType);

        Trainer updated = trainerDAO.update(updatedTrainer);

        logger.info("Trainer updated: {}", updated);
        return trainerMapper.toUpdateResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public TrainerResponse getTrainerByUsername(String username) {
        logger.debug("Getting trainer by username: {}", username);

        validator.validateUsername(username);

        return Optional.ofNullable(trainerDAO.findByUsername(username))
                .map(trainer -> {
                    logger.debug("Trainer found by username: {}", username);
                    return trainerMapper.toResponse(trainer);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainer with username '%s' not found", username)
                ));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainerResponse> getUnassignedTrainers(String traineeUsername) {
        logger.debug("Getting unassigned trainers for trainee username: {}", traineeUsername);

        validator.validateUsername(traineeUsername);

        Trainee trainee = Optional.ofNullable(traineeDAO.findByUsername(traineeUsername))
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Trainee with username '%s' not found", traineeUsername)
                ));

        Set<Trainer> assignedTrainers = trainee.getTrainers();
        List<Trainer> allTrainers = trainerDAO.getAllTrainers();

        List<Trainer> unassignedTrainers = allTrainers.stream()
                .filter(trainer -> !assignedTrainers.contains(trainer))
                .toList();

        logger.info("Found {} unassigned trainers for trainee '{}'", unassignedTrainers.size(), traineeUsername);
        return unassignedTrainers.stream()
                .map(trainerMapper::toResponse)
                .collect(Collectors.toList());
    }
}

