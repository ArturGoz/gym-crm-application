package com.gca.security;

import com.gca.actuator.prometheus.AuthenticationMetrics;
import com.gca.dto.auth.AuthenticationRequestDTO;
import com.gca.dto.auth.AuthenticationResponseDTO;
import com.gca.exception.AccountLockedException;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        activeUser = GymTestProvider.constructUser();
    }

    @Test
    void authenticate_successful() {
        when(loginAttemptService.isBlocked(request.getUsername())).thenReturn(false);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(ofNullable(activeUser));
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword()))
                .thenReturn(true);

        AuthenticationResponseDTO result = authenticationService.authenticate(request);

        assertTrue(result.isAuthenticated());
        assertEquals("User authenticated successfully", result.getMessage());
        verify(loginAttemptService).isBlocked(request.getUsername());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userService).isUserCredentialsValid(request.getUsername(), request.getPassword());
        verify(loginAttemptService).loginSucceeded(request.getUsername());
        verify(metrics).recordSuccessfulLogin();
    }

    @Test
    void authenticate_userBlocked_throwsAccountLockedException() {
        when(loginAttemptService.isBlocked(request.getUsername())).thenReturn(true);

        AccountLockedException ex = assertThrows(AccountLockedException.class, () ->
                authenticationService.authenticate(request));

        assertNotNull(ex);
        verify(loginAttemptService).isBlocked(request.getUsername());
        verifyNoMoreInteractions(loginAttemptService, userRepository, userService, metrics);
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        when(loginAttemptService.isBlocked(request.getUsername())).thenReturn(false);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                authenticationService.authenticate(request));

        assertNotNull(ex);
        verify(loginAttemptService).isBlocked(request.getUsername());
        verify(userRepository).findByUsername(request.getUsername());
    }

    @Test
    void authenticate_wrongPassword_throwsException() {
        when(loginAttemptService.isBlocked(request.getUsername())).thenReturn(false);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(activeUser));
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword())).thenReturn(false);

        UserNotAuthenticatedException ex = assertThrows(UserNotAuthenticatedException.class, () ->
                authenticationService.authenticate(request));

        assertNotNull(ex);
        verify(loginAttemptService).isBlocked(request.getUsername());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userService).isUserCredentialsValid(request.getUsername(), request.getPassword());
        verify(loginAttemptService).loginFailed(request.getUsername());
        verify(metrics).recordFailedLogin();
    }

    @Test
    void authenticate_inactiveUser_throwsException() {
        activeUser.setIsActive(false);

        when(loginAttemptService.isBlocked(request.getUsername())).thenReturn(false);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(activeUser));
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword())).thenReturn(true);

        UserNotAuthenticatedException ex = assertThrows(UserNotAuthenticatedException.class, () ->
                authenticationService.authenticate(request));

        assertNotNull(ex);
        verify(loginAttemptService).isBlocked(request.getUsername());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userService).isUserCredentialsValid(request.getUsername(), request.getPassword());
    }
}
