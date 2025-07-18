package com.gca.security;

import com.gca.dao.UserDAO;
import com.gca.dto.auth.AuthenticationRequest;
import com.gca.dto.auth.AuthenticationDTO;
import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AuthenticationService {
    private UserService userService;
    private UserDAO userDAO;
    private AuthContextHolder authContextHolder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setAuthContextHolder(AuthContextHolder authContextHolder) {
        this.authContextHolder = authContextHolder;
    }

    public AuthenticationDTO authenticate(@Valid AuthenticationRequest request) {
        User user = userDAO.findByUsername(request.getUsername());

        Optional.ofNullable(user).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (isNotAuthenticated(user, request.getPassword())) {
            throw new UserNotAuthenticatedException("Wrong user credentials");
        }

        authContextHolder.setCurrentUser(user);

        log.info("Authenticated user: {}", user.getUsername());
        return new AuthenticationDTO("User authenticated successfully", true);
    }

    private boolean isNotAuthenticated(User user, String rawPassword) {
        return !userService.isUserCredentialsValid(user.getUsername(), rawPassword)
                || !user.getIsActive();
    }
}
