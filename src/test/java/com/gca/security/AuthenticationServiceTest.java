package com.gca.security;

import com.gca.actuator.prometheus.AuthenticationMetrics;
import com.gca.dto.auth.AuthenticationRequestDTO;
import com.gca.dto.auth.AuthenticationResponseDTO;
import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.repository.UserRepository;
import com.gca.service.UserService;
import com.gca.utils.GymTestProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private final AuthenticationRequestDTO request = new AuthenticationRequestDTO(
            "arnold.schwarzenegger", "securePass123");

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationMetrics metrics;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        activeUser = GymTestProvider.constructUser();
    }

    @Test
    void authenticate_successful() {
        when(userRepository.findByUsername(request.getUsername())).thenReturn(ofNullable(activeUser));
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword()))
                .thenReturn(true);

        AuthenticationResponseDTO result = authenticationService.authenticate(request);

        assertTrue(result.isAuthenticated());
        assertEquals("User authenticated successfully", result.getMessage());
        verify(metrics).recordSuccessfulLogin();
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticate(request));
        verify(userService, never()).isUserCredentialsValid(anyString(), anyString());
    }

    @Test
    void authenticate_wrongPassword_throwsException() {
        when(userRepository.findByUsername(request.getUsername())).thenReturn(ofNullable(activeUser));
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword()))
                .thenReturn(false);

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationService.authenticate(request));
        verify(metrics).recordFailedLogin();
    }

    @Test
    void authenticate_inactiveUser_throwsException() {
        activeUser.setIsActive(false);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(ofNullable(activeUser));
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword())).thenReturn(true);

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationService.authenticate(request));
    }
}
