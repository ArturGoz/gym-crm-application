package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.service.TrainingService;
import com.gca.service.utils.ValidateHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingDAO trainingDAO;
    private TrainerDAO trainerDAO;
    private TraineeDAO traineeDAO;
    private TrainingTypeDAO trainingTypeDAO;
    private TrainingMapper trainingMapper;

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setTrainingTypeDAO(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Override
    public TrainingResponse createTraining(@Valid TrainingCreateRequest request) {
        logger.debug("Creating training {}", request.getName());

        Training training = trainingMapper.toEntity(request);

        Trainer trainer = trainerDAO.getById(request.getTrainerId());
        Trainee trainee = traineeDAO.getById(request.getTraineeId());
        TrainingType trainingType = trainingTypeDAO.getById(request.getTrainingTypeId());

        ValidateHelper.requireAllNotNull(
                Trainer.class.getName(), trainer,
                Trainee.class.getName(), trainee,
                TrainingType.class.getName(), trainingType
        );

        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setType(trainingType);

        Training created = trainingDAO.create(training);

        logger.info("Created training: {}", created);

        return trainingMapper.toResponse(created);
    }

    @Override
    public TrainingResponse getTrainingById(@NotNull Long id) {
        logger.debug("Retrieving training for id: {}", id);
        return trainingMapper.toResponse(trainingDAO.getById(id));
    }
}
