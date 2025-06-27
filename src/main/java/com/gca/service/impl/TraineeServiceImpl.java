package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.model.Trainee;
import com.gca.service.TraineeService;
import com.gca.utils.CombinedUsernameExistenceChecker;
import com.gca.utils.RandomPasswordGenerator;
import com.gca.utils.UserCreationHelper;
import com.gca.utils.UsernameGenerator;
import com.gca.utils.UsernameGeneratorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired private TraineeDAO traineeDAO;
    @Autowired private UserCreationHelper userCreationHelper;

    @Override
    public Trainee createTrainee(Trainee trainee) {
        String username = userCreationHelper.generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = userCreationHelper.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);

        logger.info("Creating trainee: {}", username);
        return traineeDAO.create(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        Trainee existing = traineeDAO.getById(trainee.getUserId());
        if (existing == null) {
            logger.error("Trainee not found for update: {}", trainee.getUserId());
            throw new RuntimeException("Trainee not found");
        }

        existing.setDateOfBirth(trainee.getDateOfBirth());
        existing.setAddress(trainee.getAddress());
        existing.setActive(trainee.isActive());

        logger.info("Updating trainee: {}", existing.getUsername());
        return traineeDAO.update(existing);
    }

    @Override
    public void deleteTrainee(Long id) {
        logger.info("Deleting trainee with ID: {}", id);
        traineeDAO.delete(id);
    }

    @Override
    public Trainee getTraineeById(Long id) {
        return traineeDAO.getById(id);
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        return traineeDAO.getByUsername(username);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDAO.getAll();
    }
}
