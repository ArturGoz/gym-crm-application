package com.gca.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AccessTokenServiceTest {

    private AccessTokenService accessTokenService;
    private String secret;
    private Long accessDuration;

    @BeforeEach
    void setUp() {
        accessTokenService = new AccessTokenService();
        secret = "ThisIsASecretKeyForJwtThatIsAtLeast32Bytes!";
        accessDuration = 3600_000L;

        ReflectionTestUtils.setField(accessTokenService, "secretKey", secret);
        ReflectionTestUtils.setField(accessTokenService, "jwtAccessDuration", accessDuration);
    }

    @Test
    void createAccessToken_shouldReturnValidJwt() {
        String username = "testUser";

        String token = accessTokenService.createAccessToken(username);

        assertNotNull(token);
        assertEquals(username, parseUsername(token));
    }

    @Test
    void getUsername_shouldExtractCorrectUsernameFromToken() {
        String username = "yurii_donets";
        String token = buildToken(username, accessDuration);

        assertEquals(username, accessTokenService.getUsername(token));
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String token = buildToken("validUser", accessDuration);
        assertTrue(accessTokenService.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        assertFalse(accessTokenService.validateToken("invalid.token.value"));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() {
        String token = buildToken("expiredUser", -5_000L);
        assertFalse(accessTokenService.validateToken(token));
    }

    private String buildToken(String username, long durationMillis) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + durationMillis))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    private String parseUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
