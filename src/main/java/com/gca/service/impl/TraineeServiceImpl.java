package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateDTO;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserCreationDTO;
import com.gca.exception.ServiceException;
import com.gca.mapper.TraineeMapper;
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

import java.util.Optional;

import static java.lang.String.format;

@Service
@Validated
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private TraineeDAO traineeDAO;
    private UserService userService;

    private TraineeMapper traineeMapper;
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
    public UserCreationDTO createTrainee(@Valid TraineeCreateRequest request) {
        logger.debug("Creating trainee");

        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        User user = userService.createUser(userCreateRequest);

        Trainee trainee = traineeMapper.toEntity(request).toBuilder()
                .user(user)
                .build();

        Trainee created = traineeDAO.create(trainee);

        logger.info("Created trainee: {}", created);
        return userMapper.toResponse(user);
    }

    @Transactional
    @Override
    public TraineeUpdateDTO updateTrainee(@Valid TraineeUpdateData request) {
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
    public TraineeDTO getTraineeByUsername(String username) {
        logger.debug("Getting trainee by username: {}", username);

        validator.validateUsername(username);

        return Optional.ofNullable(traineeDAO.findByUsername(username))
                .map(trainee -> {
                    logger.debug("Trainee found by username: {}", username);
                    return traineeMapper.toResponse(trainee);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainee with username '%s' not found", username)
                ));
    }

    @Transactional
    @Override
    public TraineeDTO updateTraineeTrainers(@Valid UpdateTraineeTrainersRequest request) {
        logger.debug("Updating trainers for trainee usernames");

        Trainee updated = traineeDAO.updateTraineeTrainers
                (request.getTraineeUsername(), request.getTrainerNames());

        logger.info("Updated trainers for trainee");
        return traineeMapper.toResponse(updated);
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

