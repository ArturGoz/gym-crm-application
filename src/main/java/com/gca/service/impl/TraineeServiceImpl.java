package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.UserDAO;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.exception.ServiceException;
import com.gca.mapper.TraineeMapper;
import com.gca.model.Trainee;
import com.gca.model.User;
import com.gca.dao.transaction.Transactional;
import com.gca.service.TraineeService;
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
    private UserDAO userDAO;
    private TraineeMapper traineeMapper;
    private CoreValidator validator;

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
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
    public TraineeResponse createTrainee(@Valid TraineeCreateRequest request) {
        logger.debug("Creating trainee");

        User user = Optional.ofNullable(userDAO.getById(request.getUserId()))
                .orElseThrow(() -> new ServiceException(
                        format("Invalid user ID: %d", request.getUserId())
                ));

        Trainee trainee = traineeMapper.toEntity(request).toBuilder()
                .user(user)
                .build();

        Trainee created = traineeDAO.create(trainee);

        logger.info("Created trainee: {}", created);
        return traineeMapper.toResponse(created);
    }

    @Transactional
    @Override
    public TraineeResponse updateTrainee(@Valid TraineeUpdateRequest request) {
        logger.debug("Updating trainee");

        Trainee trainee = Optional.ofNullable(traineeDAO.getById(request.getId()))
                .orElseThrow(() -> new ServiceException(
                        format("Invalid trainee ID: %d", request.getId())
                ));

        Trainee updatedTrainee = traineeMapper.toEntity(request).toBuilder()
                .id(trainee.getId())
                .user(trainee.getUser())
                .build();

        Trainee updated = traineeDAO.update(updatedTrainee);

        logger.info("Updated trainee: {}", updated);
        return traineeMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public TraineeResponse getTraineeById(Long id) {
        logger.debug("Retrieving trainee with ID: {}", id);

        Optional.ofNullable(id)
                .orElseThrow(() -> new ServiceException("Trainee ID must not be null"));

        Trainee trainee = Optional.ofNullable(traineeDAO.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainee with ID %d not found", id)
                ));

        return traineeMapper.toResponse(trainee);
    }

    @Transactional(readOnly = true)
    @Override
    public TraineeResponse getTraineeByUsername(String username) {
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
    public void deleteTraineeById(Long id) {
        logger.debug("Deleting trainee with ID: {}", id);

        Optional.ofNullable(id)
                .orElseThrow(() -> new ServiceException("Trainee ID must not be null"));

        Optional.ofNullable(traineeDAO.getById(id))
                .ifPresentOrElse(
                        trainee -> {
                            traineeDAO.deleteById(id);
                            logger.info("Deleted trainee with ID: {}", id);
                        },
                        () -> {
                            throw new EntityNotFoundException(
                                    format("Trainee with ID %d not found", id)
                            );
                        }
                );
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

