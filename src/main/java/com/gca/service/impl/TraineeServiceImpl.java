package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.mapper.TraineeMapper;
import com.gca.model.Trainee;
import com.gca.service.TraineeService;
import com.gca.service.helper.UserCreationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private TraineeDAO traineeDAO;
    private UserCreationHelper userCreationHelper;
    private TraineeMapper traineeMapper;

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setUserCreationHelper(UserCreationHelper userCreationHelper) {
        this.userCreationHelper = userCreationHelper;
    }

    @Autowired
    public void setTraineeMapper(TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    @Override
    public TraineeResponse createTrainee(TraineeCreateRequest request) {
        Trainee trainee = traineeMapper.toEntity(request);
        String username = userCreationHelper.generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = userCreationHelper.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setIsActive(true);

        logger.info("Creating trainee: {}", username);
        Trainee created = traineeDAO.create(trainee);

        return traineeMapper.toResponse(created);
    }

    @Override
    public TraineeResponse updateTrainee(TraineeUpdateRequest request) {
        Trainee existing = traineeDAO.getById(request.getId());
        if (existing == null) {
            throw new RuntimeException("Trainee not found");
        }

        logger.info("Updating trainee: {}", existing.getUsername());
        Trainee updated = traineeDAO.update(existing);

        return traineeMapper.toResponse(updated);
    }

    @Override
    public void deleteTrainee(Long id) {
        logger.info("Deleting trainee with ID: {}", id);
        traineeDAO.delete(id);
    }

    @Override
    public TraineeResponse getTraineeById(Long id) {
        return traineeMapper.toResponse(traineeDAO.getById(id));
    }

    @Override
    public TraineeResponse getTraineeByUsername(String username) {
        return traineeMapper.toResponse(traineeDAO.getByUsername(username));
    }

    @Override
    public List<TraineeResponse> getAllTrainees() {
        return traineeDAO.getAll().stream()
                .map(traineeMapper::toResponse)
                .collect(Collectors.toList());
    }
}
