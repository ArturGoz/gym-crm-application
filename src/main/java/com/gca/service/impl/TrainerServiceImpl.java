package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.dto.trainer.TrainerCreateDTO;
import com.gca.dto.trainer.TrainerGetDTO;
import com.gca.dto.trainer.TrainerUpdateRequestDTO;
import com.gca.dto.trainer.TrainerUpdateResponseDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.dto.user.UserCredentialsDTO;
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
import com.gca.service.common.UserProfileService;
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

    private UserProfileService userProfileService;
    private CoreValidator validator;

    @Autowired
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setTrainingTypeDAO(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
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
    public UserCredentialsDTO createTrainer(@Valid TrainerCreateDTO request) {
        logger.debug("Creating trainer");

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        User user = userService.createUser(userCreateDTO);

        String rawPass = user.getPassword();
        user.setPassword(userProfileService.encryptPassword(rawPass));

        TrainingType trainingType = trainingTypeDAO.getByName(request.getSpecialization());

        if (trainingType == null) {
            throw new ServiceException("Invalid training type");
        }

        Trainer trainer = Trainer.builder()
                .specialization(trainingType)
                .user(user)
                .build();

        Trainer created = trainerDAO.create(trainer);

        logger.info("Trainer created: {}", created);

        return userMapper.toResponse(user.toBuilder().password(rawPass).build());
    }

    @Transactional
    @Override
    public TrainerUpdateResponseDTO updateTrainer(@Valid TrainerUpdateRequestDTO request) {
        logger.debug("Updating trainer");

        Trainer trainer = Optional.ofNullable(trainerDAO.findByUsername(request.getUsername()))
                .orElseThrow(() -> new ServiceException(
                        format("Invalid trainer username: %s", request.getUsername())
                ));

        User updatedUser = trainerMapper.fillUserFields(trainer.getUser(), request);

        TrainingType trainingType = Optional.ofNullable(trainingTypeDAO.getByName(request.getSpecialization()))
                .orElse(trainer.getSpecialization());

        Trainer updatedTrainer = trainerMapper.fillTrainerFields(trainer, updatedUser, trainingType);

        Trainer updated = trainerDAO.update(updatedTrainer);

        logger.info("Trainer updated: {}", updated);
        return trainerMapper.toUpdateResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public TrainerGetDTO getTrainerByUsername(String username) {
        logger.debug("Getting trainer by username: {}", username);

        validator.validateUsername(username);

        return Optional.ofNullable(trainerDAO.findByUsername(username))
                .map(trainer -> {
                    logger.debug("Trainer found by username: {}", username);
                    return trainerMapper.toGetDto(trainer);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainer with username '%s' not found", username)
                ));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AssignedTrainerDTO> getUnassignedTrainers(String traineeUsername) {
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
                .map(trainerMapper::toAssignedDto)
                .collect(Collectors.toList());
    }
}

