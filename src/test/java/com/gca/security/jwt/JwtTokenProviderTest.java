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

    private AccessTokenService accessTokenService;

    @BeforeEach
    void setUp() {
        accessTokenService = new AccessTokenService();
        ReflectionTestUtils.setField(accessTokenService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(accessTokenService, "jwtAccessDuration", JWT_DURATION);
    }

    @Test
    void createToken_and_parseToken_ShouldReturnCorrectUsername() {
        String username = "testuser";

        String token = accessTokenService.createAccessToken(username);

        String extractedUsername = accessTokenService.getUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_withValidToken_ShouldReturnTrue() {
        String token = accessTokenService.createAccessToken("validuser");
        boolean isValid = accessTokenService.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void validateToken_withInvalidToken_ShouldReturnFalse() {
        String invalidToken = "invalid.token.string";
        boolean isValid = accessTokenService.validateToken(invalidToken);
        assertFalse(isValid);
    }
}