package com.gca.security.aspect;


import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.security.AuthContextHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationAspectTest {

    @Mock
    private AuthContextHolder authContextHolder;

    @InjectMocks
    private AuthenticationAspect authenticationAspect;

    @Test
    void checkAuthentication_shouldThrow_whenNoUserAuthenticated() {
        when(authContextHolder.getCurrentUser()).thenReturn(null);

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationAspect.checkAuthentication());
    }

    @Test
    void checkAuthentication_shouldPass_whenUserAuthenticated() {
        User mockUser = User.builder()
                .username("testUser")
                .build();
        when(authContextHolder.getCurrentUser()).thenReturn(mockUser);

        authenticationAspect.checkAuthentication();
    }
}