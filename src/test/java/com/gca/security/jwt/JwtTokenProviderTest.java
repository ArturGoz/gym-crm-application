package com.gca.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        setPrivateField(jwtTokenProvider, "secretKey", SECRET_KEY);
        setPrivateField(jwtTokenProvider, "jwtDuration", JWT_DURATION);
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

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}