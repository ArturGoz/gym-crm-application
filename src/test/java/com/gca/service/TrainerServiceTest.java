package com.gca.service;

import com.gca.dao.TrainerDAO;
import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.mapper.TrainerMapper;
import com.gca.model.Trainer;
import com.gca.service.common.UserProfileService;
import com.gca.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.gca.GymTestProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerServiceTest {

    @Mock
    private TrainerDAO dao;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private TrainerMapper mapper;

    @InjectMocks
    private TrainerServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_success() {
        TrainerCreateRequest request = GymTestProvider.trainerCreateRequest();
        Trainer trainer = GymTestProvider.trainer().toBuilder().userId(null).username(null).password(null).isActive(false).build();

        when(mapper.toEntity(request)).thenReturn(trainer);
        when(userProfileService.generateUsername("Anna", "Ivanova")).thenReturn("anna.ivanova");
        when(userProfileService.generatePassword()).thenReturn("pass123");

        Trainer trainerWithCreds = GymTestProvider.trainer();

        when(dao.create(any(Trainer.class))).thenReturn(trainerWithCreds);

        TrainerResponse expected = GymTestProvider.trainerResponse();

        when(mapper.toResponse(any(Trainer.class))).thenReturn(expected);

        TrainerResponse actual = service.createTrainer(request);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainer.class));
        verify(mapper).toResponse(any(Trainer.class));
    }

    @Test
    void updateTrainer_success() {
        TrainerUpdateRequest updateRequest = GymTestProvider.trainerUpdateRequest();
        Trainer existing = GymTestProvider.trainerInactive();
        Trainer updated = GymTestProvider.trainerUpdated();
        TrainerResponse expected = GymTestProvider.trainerResponseUpdated();

        when(dao.getById(2L)).thenReturn(existing);
        when(dao.update(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(expected);

        TrainerResponse actual = service.updateTrainer(updateRequest);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(dao).getById(2L);
        verify(dao).update(existing);
        verify(mapper).toResponse(updated);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        TrainerUpdateRequest updateRequest = GymTestProvider.trainerUpdateRequestNotFound();

        when(dao.getById(3L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainer(updateRequest));

        assertEquals("Trainer not found", ex.getMessage());
    }

    @Test
    void getTrainerById_success() {
        Trainer trainer = GymTestProvider.trainer();
        TrainerResponse expected = GymTestProvider.trainerResponse();

        when(dao.getById(2L)).thenReturn(trainer);
        when(mapper.toResponse(trainer)).thenReturn(expected);

        TrainerResponse actual = service.getTrainerById(2L);

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(dao).getById(2L);
        verify(mapper).toResponse(trainer);
    }

    @Test
    void getTrainerByUsername_success() {
        Trainer trainer = GymTestProvider.trainer();
        TrainerResponse expected = GymTestProvider.trainerResponse();

        when(dao.getByUsername("anna.ivanova")).thenReturn(trainer);
        when(mapper.toResponse(trainer)).thenReturn(expected);

        TrainerResponse actual = service.getTrainerByUsername("anna.ivanova");

        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getSpecialization(), actual.getSpecialization());
        verify(dao).getByUsername("anna.ivanova");
        verify(mapper).toResponse(trainer);
    }
}
