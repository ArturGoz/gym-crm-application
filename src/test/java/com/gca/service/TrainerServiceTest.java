package com.gca.service;

import com.gca.GymTestProvider;
import com.gca.dao.TrainerDAO;
import com.gca.dao.UserDAO;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.mapper.TrainerMapper;
import com.gca.model.Trainer;
import com.gca.service.impl.TrainerServiceImpl;
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
class TrainerServiceTest {

    @Mock
    private TrainerDAO dao;
    @Mock
    private UserDAO userDAO;
    @Mock
    private TrainerMapper mapper;

    @InjectMocks
    private TrainerServiceImpl service;

    @Test
    void createTrainer_success() {
        TrainerCreateRequest request = GymTestProvider.createTrainerCreateRequest();
        Trainer trainer = GymTestProvider.constructTrainer();

        when(mapper.toEntity(request)).thenReturn(trainer);

        Trainer trainerWithCreds = GymTestProvider.constructTrainer();

        when(dao.create(any(Trainer.class))).thenReturn(trainerWithCreds);

        TrainerResponse expected = GymTestProvider.constructTrainerResponse();

        when(mapper.toResponse(any(Trainer.class))).thenReturn(expected);

        TrainerResponse actual = service.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainer.class));
        verify(mapper).toResponse(any(Trainer.class));
    }

    @Test
    void updateTrainer_success() {
        TrainerUpdateRequest updateRequest = GymTestProvider.createTrainerUpdateRequest();
        Trainer existing = GymTestProvider.constructInactiveTrainer();
        Trainer updated = GymTestProvider.constructUpdatedTrainer();
        TrainerResponse expected = GymTestProvider.constructUpdatedTrainerResponse();

        when(dao.getById(2L)).thenReturn(existing);
        when(mapper.toEntity(updateRequest)).thenReturn(updated);
        when(dao.update(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(expected);

        TrainerResponse actual = service.updateTrainer(updateRequest);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(dao).getById(2L);
        verify(dao).update(existing);
        verify(mapper).toResponse(updated);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        TrainerUpdateRequest updateRequest = GymTestProvider.createTrainerUpdateRequestNotFound();

        when(dao.getById(3L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainer(updateRequest));

        assertEquals("Trainer not found", ex.getMessage());
    }

    @Test
    void getTrainerById_success() {
        Trainer trainer = GymTestProvider.constructTrainer();
        TrainerResponse expected = GymTestProvider.constructTrainerResponse();

        when(dao.getById(2L)).thenReturn(trainer);
        when(mapper.toResponse(trainer)).thenReturn(expected);

        TrainerResponse actual = service.getTrainerById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(dao).getById(2L);
        verify(mapper).toResponse(trainer);
    }
}
