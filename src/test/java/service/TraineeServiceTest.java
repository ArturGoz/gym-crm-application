package service;

import com.gca.dao.TraineeDAO;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.mapper.TraineeMapper;
import com.gca.model.Trainee;
import com.gca.service.common.UserProfileService;
import com.gca.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeServiceTest {

    @Mock
    private TraineeDAO dao;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private TraineeMapper mapper;

    @InjectMocks
    private TraineeServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_success() {
        // Arrange
        TraineeCreateRequest request = TraineeCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        when(mapper.toEntity(request)).thenReturn(trainee);
        when(userProfileService.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userProfileService.generatePassword()).thenReturn("pass123");

        Trainee traineeWithCreds = trainee.toBuilder()
                .username("john.doe")
                .password("pass123")
                .isActive(true)
                .build();

        when(dao.create(any(Trainee.class))).thenReturn(traineeWithCreds);

        TraineeResponse expectedResponse = TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        when(mapper.toResponse(any(Trainee.class))).thenReturn(expectedResponse);

        TraineeResponse response = service.createTrainee(request);

        assertEquals(expectedResponse, response);
        verify(mapper).toEntity(request);
        verify(dao).create(any(Trainee.class));
        verify(mapper).toResponse(any(Trainee.class));
    }

    @Test
    void updateTrainee_success() {
        TraineeUpdateRequest updateRequest = TraineeUpdateRequest.builder()
                .userId(1L)
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        Trainee existing = Trainee.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("pass123")
                .isActive(false)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        Trainee updated = existing.toBuilder()
                .isActive(true)
                .build();

        when(dao.getById(1L)).thenReturn(existing);
        when(dao.update(existing)).thenReturn(updated);

        TraineeResponse expectedResponse = TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        when(mapper.toResponse(updated)).thenReturn(expectedResponse);

        TraineeResponse result = service.updateTrainee(updateRequest);

        assertEquals(expectedResponse, result);
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

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateTrainee(updateRequest));
        assertEquals("Trainee not found", ex.getMessage());
    }

    @Test
    void deleteTrainee_success() {
        service.deleteTrainee(1L);

        verify(dao).delete(1L);
    }

    @Test
    void getTraineeById_success() {
        Trainee trainee = Trainee.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("pass123")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        TraineeResponse response = TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        when(dao.getById(1L)).thenReturn(trainee);
        when(mapper.toResponse(trainee)).thenReturn(response);

        TraineeResponse result = service.getTraineeById(1L);

        assertEquals(response, result);
        verify(dao).getById(1L);
        verify(mapper).toResponse(trainee);
    }

    @Test
    void getTraineeByUsername_success() {
        Trainee trainee = Trainee.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("pass123")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        TraineeResponse response = TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        when(dao.getByUsername("john.doe")).thenReturn(trainee);
        when(mapper.toResponse(trainee)).thenReturn(response);

        TraineeResponse result = service.getTraineeByUsername("john.doe");

        assertEquals(response, result);
        verify(dao).getByUsername("john.doe");
        verify(mapper).toResponse(trainee);
    }
}