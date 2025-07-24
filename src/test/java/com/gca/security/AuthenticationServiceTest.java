package com.gca.security;

import com.gca.dao.UserDAO;
import com.gca.dto.auth.AuthenticationRequestDTO;
import com.gca.dto.auth.AuthenticationResponseDTO;
import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.service.UserService;
import com.gca.utils.GymTestProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User activeUser;
    private AuthenticationRequestDTO request = new AuthenticationRequestDTO(
            "arnold.schwarzenegger", "securePass123");

    @BeforeEach
    void setUp() {
        activeUser = GymTestProvider.constructUser();
    }

    @Test
    void authenticate_successful() {
        when(userDAO.findByUsername(request.getUsername())).thenReturn(activeUser);
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword()))
                .thenReturn(true);

        AuthenticationResponseDTO result = authenticationService.authenticate(request);

        assertTrue(result.isAuthenticated());
        assertEquals("User authenticated successfully", result.getMessage());
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        when(userDAO.findByUsername(request.getUsername())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticate(request));
        verify(userService, never()).isUserCredentialsValid(anyString(), anyString());
    }

    @Test
    void authenticate_wrongPassword_throwsException() {
        when(userDAO.findByUsername(request.getUsername())).thenReturn(activeUser);
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword()))
                .thenReturn(false);

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void authenticate_inactiveUser_throwsException() {
        activeUser.setIsActive(false);

        when(userDAO.findByUsername(request.getUsername())).thenReturn(activeUser);
        when(userService.isUserCredentialsValid(request.getUsername(), request.getPassword())).thenReturn(true);

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationService.authenticate(request));
    }
}
