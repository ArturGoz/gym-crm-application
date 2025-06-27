package com.gca.service.impl;

import com.gca.dao.TrainingDAO;
import com.gca.model.Training;
import com.gca.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingDAO trainingDAO;

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training createTraining(Training training) {
        logger.info("Creating training for trainee: {}, trainer: {}",
                training.getTraineeId(), training.getTrainerId());

        return trainingDAO.create(training);
    }

    @Override
    public Training getTrainingById(Long id) {
        return trainingDAO.getById(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingDAO.getAll();
    }
}
