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
    private TrainerDAO trainerDAO;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerServiceImpl trainerService;

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

        when(trainerMapper.toEntity(request)).thenReturn(trainer);
        when(userProfileService.generateUsername("Anna", "Ivanova")).thenReturn("anna.ivanova");
        when(userProfileService.generatePassword()).thenReturn("pass123");

        Trainer trainerWithCreds = trainer.toBuilder()
                .username("anna.ivanova")
                .password("pass123")
                .isActive(true)
                .build();

        when(trainerDAO.create(any(Trainer.class))).thenReturn(trainerWithCreds);

        TrainerResponse expectedResponse = TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Yoga")
                .build();

        when(trainerMapper.toResponse(any(Trainer.class))).thenReturn(expectedResponse);

        TrainerResponse response = trainerService.createTrainer(request);

        assertEquals(expectedResponse, response);
        verify(trainerMapper).toEntity(request);
        verify(trainerDAO).create(any(Trainer.class));
        verify(trainerMapper).toResponse(any(Trainer.class));
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

        when(trainerDAO.getById(2L)).thenReturn(existing);
        when(trainerDAO.update(existing)).thenReturn(updated);

        TrainerResponse expectedResponse = TrainerResponse.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Ivanova")
                .username("anna.ivanova")
                .isActive(true)
                .specialization("Pilates")
                .build();

        when(trainerMapper.toResponse(updated)).thenReturn(expectedResponse);

        TrainerResponse result = trainerService.updateTrainer(updateRequest);

        assertEquals(expectedResponse, result);
        verify(trainerDAO).getById(2L);
        verify(trainerDAO).update(existing);
        verify(trainerMapper).toResponse(updated);
    }

    @Test
    void updateTrainer_notFound_throwsException() {
        TrainerUpdateRequest updateRequest = TrainerUpdateRequest.builder()
                .userId(3L)
                .isActive(true)
                .specialization("Crossfit")
                .build();

        when(trainerDAO.getById(3L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> trainerService.updateTrainer(updateRequest));
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

        when(trainerDAO.getById(2L)).thenReturn(trainer);
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = trainerService.getTrainerById(2L);

        assertEquals(response, result);
        verify(trainerDAO).getById(2L);
        verify(trainerMapper).toResponse(trainer);
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

        when(trainerDAO.getByUsername("anna.ivanova")).thenReturn(trainer);
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        TrainerResponse result = trainerService.getTrainerByUsername("anna.ivanova");

        assertEquals(response, result);
        verify(trainerDAO).getByUsername("anna.ivanova");
        verify(trainerMapper).toResponse(trainer);
    }
}
