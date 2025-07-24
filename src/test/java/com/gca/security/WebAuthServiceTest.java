package com.gca.security;

import com.gca.dao.UserDAO;
import com.gca.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebAuthServiceTest {

    @Mock
    private UserDAO userDAO;

    private WebAuthService webAuthService;

    @BeforeEach
    void setUp() {
        webAuthService = new WebAuthService(userDAO);
    }

    @Test
    void shouldReturnUserIfCookiePresentAndUserFound() {
        String username = "ronnie.coleman";
        User user = User.builder()
                .username(username)
                .build();

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        Cookie[] cookies = {new Cookie("username", username)};
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(mockRequest)
        );

        when(mockRequest.getCookies()).thenReturn(cookies);
        when(userDAO.findByUsername(username)).thenReturn(user);

        Optional<User> actual = webAuthService.getUserFromRequestContext();

        assertTrue(actual.isPresent());
        assertEquals(username, actual.get().getUsername());
    }
}
