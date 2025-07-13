package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.UserDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.exception.ServiceException;
import com.gca.mapper.TrainerMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.User;
import com.gca.service.TrainerService;
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
    private UserDAO userDAO;
    private TraineeDAO traineeDAO;

    private TrainerMapper trainerMapper;
    private CoreValidator validator;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
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

    @Transactional
    @Override
    public TrainerResponse createTrainer(@Valid TrainerCreateRequest request) {
        logger.debug("Creating trainer");

        User user = Optional.ofNullable(userDAO.getById(request.getUserId()))
                .orElseThrow(() -> new ServiceException(
                        format("Invalid user ID: %d", request.getUserId())
                ));

        Trainer trainer = trainerMapper.toEntity(request).toBuilder()
                .user(user)
                .build();

        Trainer created = trainerDAO.create(trainer);

        logger.info("Trainer created: {}", created);
        return trainerMapper.toResponse(created);
    }

    @Transactional
    @Override
    public TrainerResponse updateTrainer(@Valid TrainerUpdateRequest request) {
        logger.debug("Updating trainer");

        Trainer trainer = Optional.ofNullable(trainerDAO.getById(request.getId()))
                .orElseThrow(() -> new ServiceException(
                        format("Invalid trainer ID: %d", request.getId())
                ));

        Trainer updatedTrainer = trainerMapper.toEntity(request).toBuilder()
                .id(trainer.getId())
                .user(trainer.getUser())
                .build();

        Trainer updated = trainerDAO.update(updatedTrainer);

        logger.info("Trainer updated: {}", updated);
        return trainerMapper.toResponse(updated);
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
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
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
                .collect(Collectors.toList());

        logger.info("Found {} unassigned trainers for trainee '{}'", unassignedTrainers.size(), traineeUsername);
        return unassignedTrainers;
    }
}

