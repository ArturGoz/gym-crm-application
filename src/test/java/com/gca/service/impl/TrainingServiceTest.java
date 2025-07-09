package com.gca.service.impl;

import com.gca.GymTestProvider;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private TrainingDAO trainingDAO;

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

        when(trainingDAO.create(any(Training.class))).thenReturn(created);
        when(mapper.toResponse(any(Training.class))).thenReturn(expected);

        TrainingResponse actual = service.createTraining(request);

        assertEquals(expected, actual);
        verify(mapper).toEntity(request);
        verify(trainerDAO).getById(anyLong());
        verify(traineeDAO).getById(anyLong());
        verify(trainingTypeDAO).getById(anyLong());
        verify(trainingDAO).create(any(Training.class));
        verify(mapper).toResponse(any(Training.class));
    }

    @Test
    void getTrainingById_success() {
        Training training = GymTestProvider.constructStrengthTraining();
        TrainingResponse expected = GymTestProvider.constructStrengthTrainingResponse();

        when(trainingDAO.getById(2L)).thenReturn(training);
        when(mapper.toResponse(training)).thenReturn(expected);

        TrainingResponse actual = service.getTrainingById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDate(), actual.getDate());
        verify(trainingDAO).getById(2L);
        verify(mapper).toResponse(training);
    }
}
