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
    private TraineeDAO traineeDAO;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private TraineeMapper traineeMapper;

    @InjectMocks
    private TraineeServiceImpl traineeService;

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

        when(traineeMapper.toEntity(request)).thenReturn(trainee);
        when(userProfileService.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userProfileService.generatePassword()).thenReturn("pass123");

        Trainee traineeWithCreds = trainee.toBuilder()
                .username("john.doe")
                .password("pass123")
                .isActive(true)
                .build();

        when(traineeDAO.create(any(Trainee.class))).thenReturn(traineeWithCreds);

        TraineeResponse expectedResponse = TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        when(traineeMapper.toResponse(any(Trainee.class))).thenReturn(expectedResponse);

        TraineeResponse response = traineeService.createTrainee(request);

        assertEquals(expectedResponse, response);
        verify(traineeMapper).toEntity(request);
        verify(traineeDAO).create(any(Trainee.class));
        verify(traineeMapper).toResponse(any(Trainee.class));
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

        when(traineeDAO.getById(1L)).thenReturn(existing);
        when(traineeDAO.update(existing)).thenReturn(updated);

        TraineeResponse expectedResponse = TraineeResponse.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("address")
                .build();

        when(traineeMapper.toResponse(updated)).thenReturn(expectedResponse);

        TraineeResponse result = traineeService.updateTrainee(updateRequest);

        assertEquals(expectedResponse, result);
        verify(traineeDAO).getById(1L);
        verify(traineeDAO).update(existing);
        verify(traineeMapper).toResponse(updated);
    }

    @Test
    void updateTrainee_notFound_throwsException() {
        TraineeUpdateRequest updateRequest = TraineeUpdateRequest.builder()
                .userId(2L)
                .build();
        when(traineeDAO.getById(2L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> traineeService.updateTrainee(updateRequest));
        assertEquals("Trainee not found", ex.getMessage());
    }

    @Test
    void deleteTrainee_success() {
        traineeService.deleteTrainee(1L);

        verify(traineeDAO).delete(1L);
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

        when(traineeDAO.getById(1L)).thenReturn(trainee);
        when(traineeMapper.toResponse(trainee)).thenReturn(response);

        TraineeResponse result = traineeService.getTraineeById(1L);

        assertEquals(response, result);
        verify(traineeDAO).getById(1L);
        verify(traineeMapper).toResponse(trainee);
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

        when(traineeDAO.getByUsername("john.doe")).thenReturn(trainee);
        when(traineeMapper.toResponse(trainee)).thenReturn(response);

        TraineeResponse result = traineeService.getTraineeByUsername("john.doe");

        assertEquals(response, result);
        verify(traineeDAO).getByUsername("john.doe");
        verify(traineeMapper).toResponse(trainee);
    }
}