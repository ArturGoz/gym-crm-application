package com.gca.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtCookieServiceTest {

    private static final Long JWT_ACCESS_DURATION = 900000L;
    private static final Long JWT_REFRESH_DURATION = 604800000L;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private JwtCookieService jwtCookieService;

    @Test
    void createAccessTokenCookie_shouldCreateValidCookie() {
        String accessToken = "access-token-123";
        ReflectionTestUtils.setField(jwtCookieService, "jwtAccessDuration", JWT_ACCESS_DURATION);

        ResponseCookie cookie = jwtCookieService.createAccessTokenCookie(accessToken);

        assertEquals("JWT", cookie.getName());
        assertEquals(accessToken, cookie.getValue());
        assertEquals(Duration.ofMillis(JWT_ACCESS_DURATION), cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    void createRefreshTokenCookie_shouldCreateValidCookie() {
        String refreshToken = "refresh-token-456";
        ReflectionTestUtils.setField(jwtCookieService, "jwtRefreshDuration", JWT_REFRESH_DURATION);

        ResponseCookie cookie = jwtCookieService.createRefreshTokenCookie(refreshToken);

        assertEquals("REFRESH_TOKEN", cookie.getName());
        assertEquals(refreshToken, cookie.getValue());
        assertEquals(Duration.ofMillis(JWT_REFRESH_DURATION), cookie.getMaxAge());
    }

    @Test
    void createCleanJwtCookie_shouldCreateEmptyCookie() {
        ResponseCookie cookie = jwtCookieService.createCleanJwtCookie();

        assertEquals("JWT", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(Duration.ofMillis(0L), cookie.getMaxAge());
    }

    @Test
    void createCleanRefreshTokenCookie_shouldCreateEmptyCookie() {
        ResponseCookie cookie = jwtCookieService.createCleanRefreshTokenCookie();

        assertEquals("REFRESH_TOKEN", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(Duration.ofMillis(0L), cookie.getMaxAge());
    }

    @Test
    void extractRefreshTokenFromCookies_shouldReturnToken_whenExists() {
        String expectedToken = "valid-refresh-token";
        Cookie[] cookies = {
                new Cookie("JWT", "access-token"),
                new Cookie("REFRESH_TOKEN", expectedToken)
        };

        when(httpServletRequest.getCookies()).thenReturn(cookies);

        String actualToken = jwtCookieService.extractRefreshTokenFromCookies(httpServletRequest);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    void extractRefreshTokenFromCookies_shouldReturnNull_whenNotExists() {
        Cookie[] cookies = {new Cookie("JWT", "access-token")};

        when(httpServletRequest.getCookies()).thenReturn(cookies);

        String actualToken = jwtCookieService.extractRefreshTokenFromCookies(httpServletRequest);

        assertNull(actualToken);
    }

    @Test
    void extractRefreshTokenFromCookies_shouldReturnNull_whenCookiesAreNull() {
        when(httpServletRequest.getCookies()).thenReturn(null);
        String actualToken = jwtCookieService.extractRefreshTokenFromCookies(httpServletRequest);
        assertNull(actualToken);
    }
}