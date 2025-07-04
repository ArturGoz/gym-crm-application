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
import com.gca.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    public TraineeResponse createTrainee(TraineeCreateRequest request) {
        logger.debug("Creating trainee with user id: {}", request.getUserId());
        Trainee trainee = traineeMapper.toEntity(request);

        Long userId = request.getUserId();
        User user = userDAO.getById(userId);

        trainee = trainee.toBuilder()
                .user(user)
                .build();

        Trainee created = traineeDAO.create(trainee);
        logger.info("Created trainee: {}", created);

        return traineeMapper.toResponse(created);
    }

    @Override
    public TraineeResponse updateTrainee(TraineeUpdateRequest request) {
        logger.debug("Updating trainee");

        Trainee existing = traineeDAO.getById(request.getUserId());

        if (existing == null) {
            throw new ServiceException("Trainee not found");
        }

        Trainee updated = traineeDAO.update(existing);
        logger.info("Updated trainee: {}", updated);

        return traineeMapper.toResponse(updated);
    }

    @Override
    public void deleteTrainee(Long id) {
        logger.debug("Deleting trainee with ID: {}", id);
        traineeDAO.delete(id);
        logger.info("Deleted trainee with ID: {}", id);
    }

    @Override
    public TraineeResponse getTraineeById(Long id) {
        logger.debug("Retrieving trainee with ID: {}", id);
        return traineeMapper.toResponse(traineeDAO.getById(id));
    }
}
