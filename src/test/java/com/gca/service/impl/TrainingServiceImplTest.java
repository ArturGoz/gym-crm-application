package com.gca.service.impl;

import com.gca.GymTestProvider;
import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingDAO;
import com.gca.dao.TrainingTypeDAO;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private TrainingDAO dao;

    @Mock
    private TrainingMapper mapper;

    @InjectMocks
    private TrainingServiceImpl service;

    @Test
    void createTraining_success() {
        TrainingCreateRequest request = GymTestProvider.createTrainingCreateRequest();
        Training training = GymTestProvider.constructTrainingWithoutId();
        Training created = GymTestProvider.constructTraining();
        TrainingResponse expected = GymTestProvider.constructTrainingResponse();

        Trainer trainer = GymTestProvider.constructTrainer();
        Trainee trainee = GymTestProvider.constructTrainee();
        TrainingType type = GymTestProvider.constructTrainingType();

        when(mapper.toEntity(request)).thenReturn(training);
        when(trainerDAO.getById(anyLong())).thenReturn(trainer);
        when(traineeDAO.getById(anyLong())).thenReturn(trainee);
        when(trainingTypeDAO.getById(anyLong())).thenReturn(type);

        when(dao.create(any(Training.class))).thenReturn(created);
        when(mapper.toResponse(any(Training.class))).thenReturn(expected);

        TrainingResponse actual = service.createTraining(request);

        assertEquals(expected, actual);
        verify(mapper).toEntity(request);
        verify(trainerDAO).getById(anyLong());
        verify(traineeDAO).getById(anyLong());
        verify(trainingTypeDAO).getById(anyLong());
        verify(dao).create(any(Training.class));
        verify(mapper).toResponse(any(Training.class));
    }

    @Test
    void getTrainingById_success() {
        Training training = GymTestProvider.constructStrengthTraining();
        TrainingResponse expected = GymTestProvider.constructStrengthTrainingResponse();

        when(dao.getById(2L)).thenReturn(training);
        when(mapper.toResponse(training)).thenReturn(expected);

        TrainingResponse actual = service.getTrainingById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDate(), actual.getDate());
        verify(dao).getById(2L);
        verify(mapper).toResponse(training);
    }

    @Test
    void getTrainerTrainings_shouldReturnList_whenValidFilter() {
        TrainingTrainerCriteriaFilter filter = GymTestProvider.buildTrainerCriteriaFilter();
        Training training = GymTestProvider.constructTraining();
        List<Training> trainings = Collections.singletonList(training);
        TrainingResponse response = GymTestProvider.constructTrainingResponse();
        Trainer trainer = new Trainer();

        when(trainerDAO.getById(1L)).thenReturn(trainer);
        when(dao.getTrainerTrainings(
                eq(trainer),
                eq(filter.getFromDate()),
                eq(filter.getToDate()),
                eq(filter.getTraineeName())
        )).thenReturn(trainings);
        when(mapper.toResponse(training)).thenReturn(response);

        List<TrainingResponse> actual = service.getTrainerTrainings(filter);

        assertEquals(1, actual.size());
        assertEquals(response, actual.get(0));
        verify(trainerDAO).getById(1L);
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
                .trainerId(null)
                .build();

        ServiceException ex = assertThrows(ServiceException.class, () -> service.getTrainerTrainings(filter));

        assertEquals("Trainer ID must be provided", ex.getMessage());
        verifyNoInteractions(trainerDAO);
        verifyNoInteractions(dao);
    }

    @Test
    void getTraineeTrainings_shouldReturnList_whenValidFilter() {
        TrainingTraineeCriteriaFilter filter = GymTestProvider.buildTraineeCriteriaFilter();
        Training training = GymTestProvider.constructTraining();
        List<Training> trainings = Collections.singletonList(training);
        TrainingResponse expected = GymTestProvider.constructTrainingResponse();
        Trainee trainee = new Trainee();

        when(traineeDAO.getById(1L)).thenReturn(trainee);
        when(dao.getTraineeTrainings(
                eq(trainee),
                eq(filter.getFromDate()),
                eq(filter.getToDate()),
                eq(filter.getTrainerName()),
                eq(filter.getTrainingTypeName())
        )).thenReturn(trainings);
        when(mapper.toResponse(training)).thenReturn(expected);

        List<TrainingResponse> actual = service.getTraineeTrainings(filter);

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
        verify(traineeDAO).getById(1L);
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
                .traineeId(null)
                .build();

        ServiceException ex = assertThrows(ServiceException.class, () -> service.getTraineeTrainings(filter));

        assertEquals("Trainee ID must be provided", ex.getMessage());
        verifyNoInteractions(traineeDAO);
        verifyNoInteractions(dao);
    }
}
