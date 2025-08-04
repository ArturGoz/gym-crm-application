package com.gca.security;

import com.gca.actuator.prometheus.AuthenticationMetrics;
import com.gca.dto.auth.AuthenticationRequestDTO;
import com.gca.dto.auth.AuthenticationResponseDTO;
import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.repository.UserRepository;
import com.gca.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Component
@Slf4j
@Validated
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationMetrics metrics;

    @Transactional(readOnly = true)
    public AuthenticationResponseDTO authenticate(@Valid AuthenticationRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (isNotAuthenticated(user, request.getPassword())) {
            metrics.recordFailedLogin();
            throw new UserNotAuthenticatedException("Wrong user credentials");
        }

        metrics.recordSuccessfulLogin();
        log.info("Authenticated user: {}", user.getUsername());
        return new AuthenticationResponseDTO("User authenticated successfully", true);
    }

    private boolean isNotAuthenticated(User user, String rawPassword) {
        return !userService.isUserCredentialsValid(user.getUsername(), rawPassword)
                || !user.getIsActive();
    }
}
