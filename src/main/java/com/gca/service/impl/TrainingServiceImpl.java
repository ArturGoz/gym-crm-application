package com.gca.service.impl;

import com.gca.dao.TrainingDAO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Training;
import com.gca.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingDAO trainingDAO;
    private TrainingMapper trainingMapper;

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Override
    public TrainingResponse createTraining(TrainingCreateRequest request) {
        Training training = trainingMapper.toEntity(request);

        logger.debug("Creating training for trainee: {}, trainer: {}",
                training.getTraineeId(), training.getTrainerId());
        Training created = trainingDAO.create(training);
        logger.info("Created training: {}", created);


        return trainingMapper.toResponse(created);
    }

    @Override
    public TrainingResponse getTrainingById(Long id) {
        logger.debug("Retrieving training for id: {}", id);
        return trainingMapper.toResponse(trainingDAO.getById(id));
    }
}
