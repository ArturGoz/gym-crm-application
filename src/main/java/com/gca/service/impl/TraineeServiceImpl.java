package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeGetDTO;
import com.gca.dto.trainee.TraineeTrainersUpdateDTO;
import com.gca.dto.trainee.TraineeUpdateRequestDTO;
import com.gca.dto.trainee.TraineeUpdateResponseDTO;
import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.dto.user.UserCredentialsDTO;
import com.gca.exception.ServiceException;
import com.gca.mapper.TraineeMapper;
import com.gca.mapper.TrainerMapper;
import com.gca.mapper.UserMapper;
import com.gca.model.Trainee;
import com.gca.model.User;
import com.gca.service.TraineeService;
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
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Validated
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private TraineeDAO traineeDAO;
    private UserService userService;

    private TraineeMapper traineeMapper;
    private TrainerMapper trainerMapper;
    private UserMapper userMapper;

    private CoreValidator validator;

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTraineeMapper(TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    @Autowired
    public void setValidator(CoreValidator validator) {
        this.validator = validator;
    }

    @Transactional
    @Override
    public UserCredentialsDTO createTrainee(@Valid TraineeCreateDTO request) {
        logger.debug("Creating trainee");

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        User user = userService.createUser(userCreateDTO);

        Trainee trainee = traineeMapper.toEntity(request).toBuilder()
                .user(user)
                .build();

        Trainee created = traineeDAO.create(trainee);

        logger.info("Created trainee: {}", created);
        return userMapper.toResponse(user);
    }

    @Transactional
    @Override
    public TraineeUpdateResponseDTO updateTrainee(@Valid TraineeUpdateRequestDTO request) {
        logger.debug("Updating trainee");

        Trainee trainee = Optional.ofNullable(traineeDAO.findByUsername(request.getUsername()))
                .orElseThrow(() -> new ServiceException(
                        format("Invalid trainee username: %s", request.getUsername())
                ));

        User filledUser = traineeMapper.fillUserFields(trainee.getUser(), request);
        Trainee filledTrainee = traineeMapper.fillTraineeFields(trainee, filledUser, request);

        Trainee updated = traineeDAO.update(filledTrainee);

        logger.info("Updated trainee: {}", updated);
        return traineeMapper.toUpdateResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public TraineeGetDTO getTraineeByUsername(String username) {
        logger.debug("Getting trainee by username: {}", username);

        validator.validateUsername(username);

        return Optional.ofNullable(traineeDAO.findByUsername(username))
                .map(trainee -> {
                    logger.debug("Trainee found by username: {}", username);
                    return traineeMapper.toGetDto(trainee);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainee with username '%s' not found", username)
                ));
    }

    @Transactional
    @Override
    public List<AssignedTrainerDTO> updateTraineeTrainers(@Valid TraineeTrainersUpdateDTO request) {
        logger.debug("Updating trainers for trainee usernames");

        Trainee updated = traineeDAO.updateTraineeTrainers
                (request.getTraineeUsername(), request.getTrainerNames());

        logger.info("Updated trainers for trainee");

        return updated.getTrainers().stream()
                .map(trainerMapper::toAssignedDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteTraineeByUsername(String username) {
        logger.debug("Deleting trainee by username: {}", username);

        validator.validateUsername(username);

        Optional.ofNullable(traineeDAO.findByUsername(username))
                .ifPresentOrElse(
                        trainee -> {
                            traineeDAO.deleteByUsername(username);
                            logger.info("Deleted trainee by username: {}", username);
                        },
                        () -> {
                            throw new EntityNotFoundException(
                                    format("Trainee with username '%s' not found", username)
                            );
                        }
                );
    }
}

