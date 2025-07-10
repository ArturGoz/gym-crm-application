package com.gca.service.impl;

import com.gca.dao.TrainerDAO;
import com.gca.dao.UserDAO;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.exception.ServiceException;
import com.gca.mapper.TrainerMapper;
import com.gca.model.Trainer;
import com.gca.model.User;
import com.gca.service.TrainerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerDAO trainerDAO;
    private UserDAO userDAO;

    private TrainerMapper trainerMapper;

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

    @Override
    public TrainerResponse createTrainer(@Valid TrainerCreateRequest request) {
        logger.debug("Creating trainer");

        User user = userDAO.getById(request.getUserId());

        if (user == null) {
            throw new ServiceException("Invalid user id");
        }

        Trainer trainer = trainerMapper.toEntity(request).toBuilder()
                .user(user)
                .build();

        Trainer created = trainerDAO.create(trainer);

        logger.info("Trainer created: {}", created);
        return trainerMapper.toResponse(created);
    }

    @Override
    public TrainerResponse updateTrainer(@Valid TrainerUpdateRequest request) {
        logger.debug("Updating trainer");

        Trainer trainer = trainerDAO.getById(request.getId());

        if (trainer == null) {
            throw new ServiceException("Invalid trainer id");
        }

        Trainer updatedTrainer = trainerMapper.toEntity(request).toBuilder()
                .id(trainer.getId())
                .user(trainer.getUser())
                .build();

        Trainer updated = trainerDAO.update(updatedTrainer);

        logger.info("Trainer updated {}", updated);
        return trainerMapper.toResponse(updated);
    }

    @Override
    public TrainerResponse getTrainerByUsername(String username) {
        logger.debug("Getting trainer by username {}", username);

        if (username == null || username.trim().isEmpty()) {
            throw new ServiceException("Username must not be null or empty");
        }

        Trainer trainer = trainerDAO.findByUsername(username);
        if (trainer == null) {
            throw new EntityNotFoundException("Trainer with username " + username + " not found");
        }

        logger.debug("Trainer is found by username {}", username);
        return trainerMapper.toResponse(trainer);
    }

    @Override
    public TrainerResponse getTrainerById(Long id) {
        logger.debug("Retrieving trainer by id: {}", id);

        if (id == null) {
            throw new ServiceException("ID must not be null");
        }

        Trainer trainer = trainerDAO.getById(id);
        if (trainer == null) {
            throw new EntityNotFoundException("Trainer with id " + id + " not found");
        }

        return trainerMapper.toResponse(trainer);
    }
}
