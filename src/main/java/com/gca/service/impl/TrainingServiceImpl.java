package com.gca.service.impl;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.exception.ServiceException;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.service.TrainingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

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

    @Transactional
    @Override
    public TrainingResponse createTraining(@Valid TrainingCreateRequest request) {
        logger.debug("Creating training '{}'", request.getName());

        Training training = trainingMapper.toEntity(request);

        Trainer trainer = Optional.ofNullable(trainerDAO.getById(request.getTrainerId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainer with ID %d not found", request.getTrainerId())
                ));

        Trainee trainee = Optional.ofNullable(traineeDAO.getById(request.getTraineeId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainee with ID %d not found", request.getTraineeId())
                ));

        TrainingType trainingType = Optional.ofNullable(trainingTypeDAO.getById(request.getTrainingTypeId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Training type with ID %d not found", request.getTrainingTypeId())
                ));

        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setType(trainingType);

        Training created = trainingDAO.create(training);

        logger.info("Created training: {}", created);

        return trainingMapper.toResponse(created);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainingResponse> getTraineeTrainings(@Valid TrainingTraineeCriteriaFilter filter) {
        logger.debug("Filtering trainings by trainee");

        Trainee trainee = Optional.ofNullable(filter.getTraineeId())
                .map(traineeDAO::getById)
                .orElseThrow(() -> new ServiceException("Trainee ID must be provided"));

        List<Training> trainings = trainingDAO.getTraineeTrainings(
                trainee,
                filter.getFromDate(),
                filter.getToDate(),
                filter.getTrainerName(),
                filter.getTrainingTypeName()
        );

        return trainings.stream()
                .map(trainingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainingResponse> getTrainerTrainings(@Valid TrainingTrainerCriteriaFilter filter) {
        logger.debug("Filtering trainings by trainer");

        Trainer trainer = Optional.ofNullable(filter.getTrainerId())
                .map(trainerDAO::getById)
                .orElseThrow(() -> new ServiceException("Trainer ID must be provided"));

        List<Training> trainings = trainingDAO.getTrainerTrainings(
                trainer,
                filter.getFromDate(),
                filter.getToDate(),
                filter.getTraineeName()
        );

        return trainings.stream()
                .map(trainingMapper::toResponse)
                .toList();
    }
}
