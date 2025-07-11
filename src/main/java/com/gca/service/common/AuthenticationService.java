package com.gca.service.common;

import com.gca.dao.UserDAO;
import com.gca.dto.auth.AuthenticationRequest;
import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationService {
    private UserService userService;
    private UserDAO userDAO;
    private AuthContextHolder authContextHolder ;

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

    public boolean authenticate(@Valid AuthenticationRequest request) {
        User user = userDAO.findByUsername(request.getUsername());

        Optional.ofNullable(user).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!userService.isUserCredentialsValid(user.getUsername(), request.getPassword()) || !user.getIsActive()) {
            throw new UserNotAuthenticatedException("Wrong user credentials");
        }

        authContextHolder.setCurrentUser(user);
        return true;
    }
}
