package com.gca.security.jwt;

import com.gca.exception.TokenRefreshException;
import com.gca.model.jwt.RefreshToken;
import com.gca.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "jwtRefreshDuration", 3600_000L);
    }

    @Test
    void findByToken_shouldReturnOptionalRefreshToken() {
        String token = "test-token";
        RefreshToken refreshToken = RefreshToken.builder().token(token).build();

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertTrue(result.isPresent());
        assertEquals(token, result.get().getToken());
        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void createRefreshToken_shouldDeleteOldAndSaveNew() {
        String username = "user123";
        RefreshToken savedToken = RefreshToken.builder()
                .username(username)
                .token("new-token")
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(savedToken);

        RefreshToken result = refreshTokenService.createRefreshToken(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertNotNull(result.getToken());
        assertTrue(result.getExpiryDate().isAfter(Instant.now()));
        verify(refreshTokenRepository).deleteByUsername(username);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void verifyExpiration_shouldReturnToken_whenNotExpired() {
        RefreshToken token = RefreshToken.builder()
                .token("valid-token")
                .expiryDate(Instant.now().plusSeconds(60))
                .build();

        RefreshToken result = refreshTokenService.verifyExpiration(token);

        assertSame(token, result);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void verifyExpiration_shouldThrowException_whenExpired() {
        RefreshToken token = RefreshToken.builder()
                .token("expired-token")
                .expiryDate(Instant.now().minusSeconds(60))
                .build();

        TokenRefreshException ex = assertThrows(
                TokenRefreshException.class,
                () -> refreshTokenService.verifyExpiration(token)
        );

        assertTrue(ex.getMessage().contains("Refresh token was expired"));
        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteByUsername_shouldCallRepositoryDelete() {
        String username = "user123";
        refreshTokenService.deleteByUsername(username);
        verify(refreshTokenRepository).deleteByUsername(username);
    }
}
