package service;

import com.gca.dao.TrainingDAO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Training;
import com.gca.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import provider.GymTestProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    @Mock
    private TrainingDAO dao;
    @Mock
    private TrainingMapper mapper;

    @InjectMocks
    private TrainingServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_success() {
        // given
        TrainingCreateRequest request = GymTestProvider.trainingCreateRequest();
        Training training = GymTestProvider.trainingWithoutId();
        Training created = GymTestProvider.training();
        TrainingResponse expectedResponse = GymTestProvider.trainingResponse();

        when(mapper.toEntity(request)).thenReturn(training);
        when(dao.create(any(Training.class))).thenReturn(created);
        when(mapper.toResponse(any(Training.class))).thenReturn(expectedResponse);

        // when
        TrainingResponse response = service.createTraining(request);

        // then
        assertEquals(expectedResponse, response);
        verify(mapper).toEntity(request);
        verify(dao).create(any(Training.class));
        verify(mapper).toResponse(any(Training.class));
    }

    @Test
    void getTrainingById_success() {
        // given
        Training training = GymTestProvider.trainingStrength();
        TrainingResponse response = GymTestProvider.trainingResponseStrength();

        when(dao.getById(2L)).thenReturn(training);
        when(mapper.toResponse(training)).thenReturn(response);

        // when
        TrainingResponse result = service.getTrainingById(2L);

        // then
        assertEquals(response, result);
        verify(dao).getById(2L);
        verify(mapper).toResponse(training);
    }
}
