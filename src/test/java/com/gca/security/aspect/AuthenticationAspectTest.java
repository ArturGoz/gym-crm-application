package com.gca.security.aspect;


import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.security.WebAuthService;
import com.gca.utils.GymTestProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationAspectTest {

    @Mock
    private WebAuthService webAuthService;

    @InjectMocks
    private AuthenticationAspect authenticationAspect;

    @Test
    void checkAuthentication_shouldThrow_whenNoUserAuthenticated() {
        when(webAuthService.getUserFromRequestContext()).thenReturn(Optional.empty());

        assertThrows(UserNotAuthenticatedException.class, () -> authenticationAspect.checkAuthentication());
    }

    @Test
    void checkAuthentication_shouldPass_whenUserAuthenticated() {
        User mockUser = GymTestProvider.constructUser();

        when(webAuthService.getUserFromRequestContext()).thenReturn(Optional.ofNullable(mockUser));

        authenticationAspect.checkAuthentication();
    }
}
