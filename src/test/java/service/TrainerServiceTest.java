package service;

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
        TrainerCreateRequest request = TrainerCreateRequest.builder()
                .firstName("Anna")
                .lastName("Ivanova")
                .specialization("Yoga")
                .build();

        Trainer trainer = Trainer.builder()
                .firstName("Anna")
                .lastName("Ivanova")
                .specialization("Yoga")
                .build();

        when(mapper.toEntity(request)).thenReturn(trainer);
        when(userProfileService.generateUsername("Anna", "Ivanova")).thenReturn("anna.ivanova");
        when(userProfileService.generatePassword()).thenReturn("pass123");

        Trainer trainerWithCreds = trainer.toBuilder()
                .username("anna.ivanova")
                .password("pass123")
                .isActive(true)
                .build();

        when(dao.create(any(Trainer.class))).thenReturn(trainerWithCreds);

        TrainerResponse expectedResponse = TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Yoga")
                .build();

        when(mapper.toResponse(any(Trainer.class))).thenReturn(expectedResponse);

        TrainerResponse response = service.createTrainer(request);

        assertEquals(expectedResponse, response);
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainer.class));
        verify(mapper).toResponse(any(Trainer.class));
    }

    @Test
    void updateTrainer_success() {
        TrainerUpdateRequest updateRequest = TrainerUpdateRequest.builder()
                .userId(2L)
                .isActive(true)
                .specialization("Pilates")
                .build();

        Trainer existing = Trainer.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .password("pass123")
                .isActive(false)
                .specialization("Yoga")
                .build();

        Trainer updated = existing.toBuilder()
                .isActive(true)
                .specialization("Pilates")
                .build();

        when(dao.getById(2L)).thenReturn(existing);
        when(dao.update(existing)).thenReturn(updated);

        TrainerResponse expectedResponse = TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Pilates")
                .build();

        when(mapper.toResponse(updated)).thenReturn(expectedResponse);

        TrainerResponse result = service.updateTrainer(updateRequest);

        assertEquals(expectedResponse, result);
        verify(dao).getById(2L);
        verify(dao).update(existing);
        verify(mapper).toResponse(updated);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        TrainerUpdateRequest updateRequest = TrainerUpdateRequest.builder()
                .userId(3L)
                .isActive(true)
                .specialization("Crossfit")
                .build();

        when(dao.getById(3L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateTrainer(updateRequest));
        assertEquals("Trainer not found", ex.getMessage());
    }

    @Test
    void getTrainerById_success() {
        Trainer trainer = Trainer.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .password("pass123")
                .isActive(true)
                .specialization("Yoga")
                .build();

        TrainerResponse response = TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Yoga")
                .build();

        when(dao.getById(2L)).thenReturn(trainer);
        when(mapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = service.getTrainerById(2L);

        assertEquals(response, result);
        verify(dao).getById(2L);
        verify(mapper).toResponse(trainer);
    }

    @Test
    void getTrainerByUsername_success() {
        Trainer trainer = Trainer.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .password("pass123")
                .isActive(true)
                .specialization("Yoga")
                .build();

        TrainerResponse response = TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Yoga")
                .build();

        when(dao.getByUsername("anna.ivanova")).thenReturn(trainer);
        when(mapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = service.getTrainerByUsername("anna.ivanova");

        assertEquals(response, result);
        verify(dao).getByUsername("anna.ivanova");
        verify(mapper).toResponse(trainer);
    }
}
