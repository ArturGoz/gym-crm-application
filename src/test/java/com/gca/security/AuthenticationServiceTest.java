package com.gca.security;

import com.gca.dao.UserDAO;
import com.gca.dto.auth.AuthenticationRequest;
import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthContextHolder authContextHolder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        activeUser = new User();
        activeUser.setId(1L);
        activeUser.setUsername("john");
        activeUser.setIsActive(true);
    }

    @Test
    void authenticate_successful() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("john");
        request.setPassword("password");

        when(userDAO.findByUsername("john")).thenReturn(activeUser);
        when(userService.isUserCredentialsValid("john", "password")).thenReturn(true);

        boolean result = authenticationService.authenticate(request);

        assertTrue(result);
        verify(authContextHolder).setCurrentUser(activeUser);
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("john");
        request.setPassword("password");

        when(userDAO.findByUsername("john")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticate(request));

        verify(authContextHolder, never()).setCurrentUser(any());
    }

    @Test
    void authenticate_invalidCredentials_throwsException() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("john");
        request.setPassword("wrongpassword");

        when(userDAO.findByUsername("john")).thenReturn(activeUser);
        when(userService.isUserCredentialsValid("john", "wrongpassword")).thenReturn(false);

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationService.authenticate(request));

        verify(authContextHolder, never()).setCurrentUser(any());
    }

    @Test
    void authenticate_inactiveUser_throwsException() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("john");
        request.setPassword("password");

        activeUser.setIsActive(false);

        when(userDAO.findByUsername("john")).thenReturn(activeUser);
        when(userService.isUserCredentialsValid("john", "password")).thenReturn(true);

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationService.authenticate(request));

        verify(authContextHolder, never()).setCurrentUser(any());
    }
}
