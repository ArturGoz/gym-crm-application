package com.gca.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    private static final String SECRET_KEY = "mysupersecretkeymysupersecretkey1234";
    private static final long JWT_DURATION = 3600000L;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtDuration", JWT_DURATION);
    }

    @Test
    void createToken_and_parseToken_ShouldReturnCorrectUsername() {
        String username = "testuser";

        String token = jwtTokenProvider.createToken(username);

        String extractedUsername = jwtTokenProvider.getUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_withValidToken_ShouldReturnTrue() {
        String token = jwtTokenProvider.createToken("validuser");
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void validateToken_withInvalidToken_ShouldReturnFalse() {
        String invalidToken = "invalid.token.string";
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        assertFalse(isValid);
    }
}