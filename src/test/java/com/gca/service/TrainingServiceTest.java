package com.gca.service;

import com.gca.GymTestProvider;
import com.gca.dao.TrainingDAO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Training;
import com.gca.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

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

        when(mapper.toEntity(request)).thenReturn(training);
        when(dao.create(any(Training.class))).thenReturn(created);
        when(mapper.toResponse(any(Training.class))).thenReturn(expected);

        TrainingResponse actual = service.createTraining(request);

        assertEquals(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        verify(mapper).toEntity(request);
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
}
