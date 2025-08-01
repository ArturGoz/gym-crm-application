package com.gca.service.impl;

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
import com.gca.repository.TraineeRepository;
import com.gca.repository.TrainerRepository;
import com.gca.repository.TrainingRepository;
import com.gca.repository.TrainingTypeRepository;
import com.gca.utils.GymTestProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingRepository dao;

    @Mock
    private TrainingMapper mapper;

    @InjectMocks
    private TrainingServiceImpl service;

    @Test
    void createTraining_success() {
        TrainingCreateDTO request = GymTestProvider.createTrainingCreateRequestDTO();
        Training training = GymTestProvider.constructTrainingWithoutId();
        Training created = GymTestProvider.constructTraining();
        TrainingDTO expected = GymTestProvider.createTrainingDTO();

        Trainer trainer = GymTestProvider.constructTrainer();
        Trainee trainee = GymTestProvider.constructTrainee();
        TrainingType type = GymTestProvider.createTrainingTypeStrength();

        when(mapper.toEntity(request)).thenReturn(training);
        when(trainerRepository.findByUsername(request.getTrainerUsername())).
                thenReturn(ofNullable(trainer));
        when(traineeRepository.findByUsername(request.getTraineeUsername()))
                .thenReturn(ofNullable(trainee));
        when(trainingTypeRepository.findByName(request.getTrainingName()))
                .thenReturn(ofNullable(type));

        when(dao.save(any(Training.class))).thenReturn(created);
        when(mapper.toResponse(any(Training.class))).thenReturn(expected);

        TrainingDTO actual = service.createTraining(request);

        assertEquals(expected, actual);
        verify(mapper).toEntity(request);
        verify(trainerRepository).findByUsername(anyString());
        verify(traineeRepository).findByUsername(anyString());
        verify(trainingTypeRepository).findByName(anyString());
        verify(dao).save(any(Training.class));
        verify(mapper).toResponse(any(Training.class));
    }

    @Test
    void getTrainerTrainings_shouldReturnList_whenValidFilter() {
        TrainingTrainerCriteriaFilter filter = GymTestProvider.buildTrainerCriteriaFilter();
        Training training = GymTestProvider.constructTraining();
        List<Training> trainings = Collections.singletonList(training);
        TrainingDTO response = GymTestProvider.createTrainingDTO();
        Trainer trainer = new Trainer();

        when(trainerRepository.findByUsername(filter.getTrainerUsername()))
                .thenReturn(Optional.of(trainer));
        when(dao.getTrainerTrainings(
                eq(trainer),
                eq(filter.getFromDate()),
                eq(filter.getToDate()),
                eq(filter.getTraineeName())
        )).thenReturn(trainings);
        when(mapper.toResponse(training)).thenReturn(response);

        List<TrainingDTO> actual = service.getTrainerTrainings(filter);

        assertEquals(1, actual.size());
        assertEquals(response, actual.get(0));
        verify(trainerRepository).findByUsername(anyString());
        verify(dao).getTrainerTrainings(
                trainer,
                filter.getFromDate(),
                filter.getToDate(),
                filter.getTraineeName()
        );
        verify(mapper).toResponse(training);
    }

    @Test
    void getTrainerTrainings_shouldThrow_whenIdNull() {
        TrainingTrainerCriteriaFilter filter = TrainingTrainerCriteriaFilter.builder()
                .trainerUsername(null)
                .build();

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.getTrainerTrainings(filter));

        assertEquals("Trainer username must be provided", ex.getMessage());
        verifyNoInteractions(dao);
    }

    @Test
    void getTraineeTrainings_shouldReturnList_whenValidFilter() {
        TrainingTraineeCriteriaFilter filter = GymTestProvider.buildTraineeCriteriaFilter();
        Training training = GymTestProvider.constructTraining();
        List<Training> trainings = Collections.singletonList(training);
        TrainingDTO expected = GymTestProvider.createTrainingDTO();
        Trainee trainee = new Trainee();

        when(traineeRepository.findByUsername(filter.getTraineeUsername()))
                .thenReturn(Optional.of(trainee));
        when(dao.getTraineeTrainings(
                eq(trainee),
                eq(filter.getFromDate()),
                eq(filter.getToDate()),
                eq(filter.getTrainerName()),
                eq(filter.getTrainingTypeName())
        )).thenReturn(trainings);
        when(mapper.toResponse(training)).thenReturn(expected);

        List<TrainingDTO> actual = service.getTraineeTrainings(filter);

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
        verify(traineeRepository).findByUsername(anyString());
        verify(dao).getTraineeTrainings(
                trainee,
                filter.getFromDate(),
                filter.getToDate(),
                filter.getTrainerName(),
                filter.getTrainingTypeName()
        );
        verify(mapper).toResponse(training);
    }

    @Test
    void getTraineeTrainings_shouldThrow_whenIdNull() {
        TrainingTraineeCriteriaFilter filter = TrainingTraineeCriteriaFilter.builder()
                .traineeUsername(null)
                .build();

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.getTraineeTrainings(filter));

        assertEquals("Trainee username must be provided", ex.getMessage());
        verifyNoInteractions(dao);
    }

    @Test
    void createTraining_shouldThrow_whenTrainerNotFound() {
        TrainingCreateDTO request = GymTestProvider.createTrainingCreateRequestDTO();

        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.createTraining(request));

        assertTrue(ex.getMessage().contains("Trainer with username"));
    }

    @Test
    void createTraining_shouldThrow_whenTrainingTypeNotFound() {
        TrainingCreateDTO request = GymTestProvider.createTrainingCreateRequestDTO();

        when(trainerRepository.findByUsername(request.getTrainerUsername()))
                .thenReturn(ofNullable(GymTestProvider.constructTrainer()));
        when(traineeRepository.findByUsername(request.getTraineeUsername()))
                .thenReturn(ofNullable(GymTestProvider.constructTrainee()));
        when(trainingTypeRepository.findByName(request.getTrainingName())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.createTraining(request));

        assertTrue(ex.getMessage().contains("Training type with name"));
    }

    @Test
    void createTraining_shouldThrow_whenTraineeNotFound() {
        TrainingCreateDTO request = GymTestProvider.createTrainingCreateRequestDTO();

        when(trainerRepository.findByUsername(request.getTrainerUsername()))
                .thenReturn(ofNullable(GymTestProvider.constructTrainer()));
        when(traineeRepository.findByUsername(request.getTraineeUsername())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.createTraining(request));

        assertTrue(ex.getMessage().contains("Trainee with username"));
    }
}

