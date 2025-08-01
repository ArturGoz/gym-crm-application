package com.gca.service.impl;

import com.gca.repository.TraineeRepository;
import com.gca.repository.TrainerRepository;
import com.gca.repository.TrainingRepository;
import com.gca.repository.TrainingTypeRepository;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.training.TrainingCreateDTO;
import com.gca.dto.training.TrainingDTO;
import com.gca.exception.ServiceException;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.service.TrainingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.lang.String.format;

@Service
@Validated
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    private final TrainingMapper trainingMapper;

    @Transactional
    @Override
    public TrainingDTO createTraining(@Valid TrainingCreateDTO request) {
        logger.debug("Creating training '{}'", request.getTrainingName());

        Training training = trainingMapper.toEntity(request);

        Trainer trainer = trainerRepository.findByUsername(request.getTrainerUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainer with username %s not found", request.getTrainerUsername())
                ));

        Trainee trainee = traineeRepository.findByUsername(request.getTraineeUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Trainee with username %s not found", request.getTraineeUsername())
                ));

        TrainingType trainingType = trainingTypeRepository.findByName(request.getTrainingName())
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Training type with name %s not found", request.getTrainingName())
                ));

        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setType(trainingType);

        Training created = trainingRepository.save(training);

        logger.info("Created training: {}", created);
        return trainingMapper.toResponse(created);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainingDTO> getTraineeTrainings(@Valid TrainingTraineeCriteriaFilter filter) {
        logger.debug("Filtering trainings by trainee");

        Trainee trainee = traineeRepository.findByUsername(filter.getTraineeUsername())
                .orElseThrow(() -> new ServiceException("Trainee username must be provided"));

        List<Training> trainings = trainingRepository.getTraineeTrainings(
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
    public List<TrainingDTO> getTrainerTrainings(@Valid TrainingTrainerCriteriaFilter filter) {
        logger.debug("Filtering trainings by trainer");

        Trainer trainer = trainerRepository.findByUsername(filter.getTrainerUsername())
                .orElseThrow(() -> new ServiceException("Trainer username must be provided"));

        List<Training> trainings = trainingRepository.getTrainerTrainings(
                trainer,
                filter.getFromDate(),
                filter.getToDate(),
                filter.getTraineeName()
        );

        return trainings.stream()
                .map(trainingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainingType> getAllTrainingTypes() {
        logger.debug("Getting all training types");
        return trainingTypeRepository.findAll();
    }
}
