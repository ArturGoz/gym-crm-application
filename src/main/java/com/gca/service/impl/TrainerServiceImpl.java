package com.gca.service.impl;

import com.gca.dao.TrainerDAO;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.mapper.TrainerMapper;
import com.gca.service.TrainerService;
import com.gca.service.common.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerDAO trainerDAO;
    private UserProfileService userProfileService;
    private TrainerMapper trainerMapper;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setUserCreationHelper(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Autowired
    public void setTrainerMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    @Override
    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        return null;
    }

    @Override
    public TrainerResponse updateTrainer(TrainerUpdateRequest request) {
        return null;
    }

    @Override
    public TrainerResponse getTrainerById(Long id) {
        logger.debug("Retrieving trainer by id: {}", id);
        return trainerMapper.toResponse(trainerDAO.getById(id));
    }

    @Override
    public TrainerResponse getTrainerByUsername(String username) {
        logger.debug("Retrieving trainer by username: {}", username);
        return null;
    }
}
