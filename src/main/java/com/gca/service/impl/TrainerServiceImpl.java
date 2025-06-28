package com.gca.service.impl;

import com.gca.dao.TrainerDAO;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.mapper.TrainerMapper;
import com.gca.model.Trainer;
import com.gca.service.TrainerService;
import com.gca.service.helper.UserCreationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerDAO trainerDAO;
    private UserCreationHelper userCreationHelper;
    private TrainerMapper trainerMapper;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setUserCreationHelper(UserCreationHelper userCreationHelper) {
        this.userCreationHelper = userCreationHelper;
    }

    @Autowired
    public void setTrainerMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    @Override
    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        Trainer trainer = trainerMapper.toEntity(request);
        String username = userCreationHelper.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = userCreationHelper.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setIsActive(true);

        logger.info("Creating trainer: {}", username);
        Trainer created = trainerDAO.create(trainer);

        return trainerMapper.toResponse(created);
    }

    @Override
    public TrainerResponse updateTrainer(TrainerUpdateRequest request) {
        Trainer existing = trainerDAO.getById(request.getId());
        if (existing == null) {
            throw new RuntimeException("Trainer not found");
        }

        logger.info("Updating trainer: {}", existing.getUsername());
        Trainer updated = trainerDAO.update(existing);

        return trainerMapper.toResponse(updated);
    }

    @Override
    public TrainerResponse getTrainerById(Long id) {
        return trainerMapper.toResponse(trainerDAO.getById(id));
    }

    @Override
    public TrainerResponse getTrainerByUsername(String username) {
        return trainerMapper.toResponse(trainerDAO.getByUsername(username));
    }

    @Override
    public List<TrainerResponse> getAllTrainers() {
        return trainerDAO.getAll().stream()
                .map(trainerMapper::toResponse)
                .collect(Collectors.toList());
    }
}
