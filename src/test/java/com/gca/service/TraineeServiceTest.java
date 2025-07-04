package com.gca.service;

import com.gca.GymTestProvider;
import com.gca.dao.TraineeDAO;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.mapper.TraineeMapper;
import com.gca.model.Trainee;
import com.gca.service.common.UserProfileService;
import com.gca.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO dao;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private TraineeMapper mapper;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_success() {
        TraineeCreateRequest request = GymTestProvider.createTraineeCreateRequest();
        Trainee trainee = GymTestProvider.constructTrainee();
        Trainee traineeWithCreds = GymTestProvider.constructTrainee();
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(mapper.toEntity(request)).thenReturn(trainee);
        when(userProfileService.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userProfileService.generatePassword()).thenReturn("pass123");
        when(dao.create(any(Trainee.class))).thenReturn(traineeWithCreds);
        when(mapper.toResponse(any(Trainee.class))).thenReturn(expected);

        TraineeResponse actual = service.createTrainee(request);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainee.class));
        verify(mapper).toResponse(any(Trainee.class));
    }

    @Test
    void updateTrainee_success() {
        TraineeUpdateRequest updateRequest = GymTestProvider.createTraineeUpdateRequest();
        Trainee existing = GymTestProvider.constructTrainee();
        Trainee updated = existing;
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(dao.getById(1L)).thenReturn(existing);
        when(dao.update(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(expected);

        TraineeResponse actual = service.updateTrainee(updateRequest);

        assertEquals(expected, actual);
        verify(dao).getById(1L);
        verify(dao).update(existing);
        verify(mapper).toResponse(updated);
    }

    @Test
    void updateTrainee_notFound_throwsException() {
        TraineeUpdateRequest updateRequest = TraineeUpdateRequest.builder()
                .userId(2L)
                .build();

        when(dao.getById(2L)).thenReturn(null);


        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainee(updateRequest));
        assertEquals("Trainee not found", ex.getMessage());
    }

    @Test
    void deleteTrainee_success() {
        service.deleteTrainee(1L);

        verify(dao).delete(1L);
    }

    @Test
    void getTraineeById_success() {
        Trainee trainee = GymTestProvider.constructTrainee();
        TraineeResponse expected = GymTestProvider.constructTraineeResponse();

        when(dao.getById(1L)).thenReturn(trainee);
        when(mapper.toResponse(trainee)).thenReturn(expected);

        TraineeResponse actual = service.getTraineeById(1L);

        assertEquals(expected, actual);
        assertEquals(expected.getAddress(), actual.getAddress());
        verify(dao).getById(1L);
        verify(mapper).toResponse(trainee);
    }
}