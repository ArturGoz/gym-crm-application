package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.UserDAO;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.mapper.TraineeMapper;
import com.gca.model.Trainee;
import com.gca.model.User;
import com.gca.service.TraineeService;
import com.gca.service.utils.ValidateHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private TraineeDAO traineeDAO;
    private UserDAO userDAO;

    private TraineeMapper traineeMapper;

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

    @Override
    public TraineeResponse createTrainee(@Valid TraineeCreateRequest request) {
        logger.debug("Creating trainee");

        User user = userDAO.getById(request.getUserId());

        ValidateHelper.requireNotNull(user, "User not found");

        Trainee trainee = traineeMapper.toEntity(request).toBuilder()
                .user(user)
                .build();

        Trainee created = traineeDAO.create(trainee);

        logger.info("Created trainee: {}", created);
        return traineeMapper.toResponse(created);
    }

    @Override
    public TraineeResponse updateTrainee(@Valid TraineeUpdateRequest request) {
        logger.debug("Updating trainee");

        Trainee trainee = traineeDAO.getById(request.getId());

        ValidateHelper.requireNotNull(trainee, "Trainee not found");

        Trainee updatedTrainee = traineeMapper.toEntity(request).toBuilder()
                .id(trainee.getId())
                .user(trainee.getUser())
                .build();

        Trainee updated = traineeDAO.update(updatedTrainee);

        logger.info("Trainee updated  {}", updated);
        return traineeMapper.toResponse(updated);
    }

    @Override
    public TraineeResponse getTraineeById(@NotNull Long id) {
        logger.debug("Retrieving trainee with ID: {}", id);
        return traineeMapper.toResponse(traineeDAO.getById(id));
    }

    @Override
    public TraineeResponse getTraineeByUsername(@NotBlank String username) {
        logger.debug("Getting trainee by username {}", username);

        Trainee trainee = traineeDAO.findByUsername(username);

        logger.debug("Trainee is found by username {}", username);
        return traineeMapper.toResponse(trainee);
    }

    @Override
    public void deleteTraineeById(@NotNull Long id) {
        logger.debug("Deleting trainee with ID: {}", id);
        traineeDAO.deleteById(id);
        logger.info("Deleted trainee with ID: {}", id);
    }

    @Override
    public void deleteTraineeByUsername(@NotBlank String username) {
        logger.debug("Deleting trainee by username {}", username);
        traineeDAO.deleteByUsername(username);
        logger.info("Deleted trainee by username {}", username);
    }
}
