package com.gca.service.impl;

import com.gca.dao.UserDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.dto.PasswordChangeDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.exception.ServiceException;
import com.gca.mapper.UserMapper;
import com.gca.model.User;
import com.gca.service.UserService;
import com.gca.service.common.UserProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static java.lang.String.format;

@Service
@Validated
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDAO userDAO;
    private UserProfileService userProfileService;
    private UserMapper userMapper;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(@Valid UserCreateDTO request) {
        logger.debug("Creating user for {} {}", request.getFirstName(), request.getLastName());

        String username = userProfileService.generateUsername(request.getFirstName(), request.getLastName());
        String password = userProfileService.generatePassword();
        User user = userMapper.toEntity(request).toBuilder()
                .username(username)
                .password(password)
                .isActive(true)
                .build();

        logger.info("User: {}", user);
        return user;
    }

    @Autowired
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public boolean isUserCredentialsValid(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return false;
        }

        return Optional.ofNullable(userDAO.findByUsername(username))
                .map(user -> userProfileService.verifyPassword(rawPassword, user.getPassword()))
                .orElse(false);
    }

    @Transactional
    @Override
    public void changeUserPassword(@Valid PasswordChangeDTO passwordChangeDTO) {
        String username = passwordChangeDTO.getUsername();
        logger.debug("Changing password for user with username: {}", username);

        User user = Optional.ofNullable(userDAO.findByUsername(username))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User with username %s not found", username)
                ));
        if (!userProfileService.verifyPassword(passwordChangeDTO.getOldPassword(), user.getPassword())) {
            throw new ServiceException("Old password is wrong");
        }

        user.setPassword(userProfileService.encryptPassword(passwordChangeDTO.getNewPassword()));
        userDAO.update(user);

        logger.info("Changed password for user with username: {}", username);
    }

    @Transactional
    @Override
    public void toggleActiveStatus(String username, boolean isActive) {
        logger.debug("Toggling active status for user with username: {}", username);

        User user = Optional.ofNullable(userDAO.findByUsername(username))
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with username %s not found", username)
                ));

        validateActiveStatus(user, isActive);
        user.setIsActive(isActive);

        User updatedUser = userDAO.update(user);
        logger.info("Toggled active status for user with username: {} to {}", username, updatedUser.getIsActive());
    }

    private void validateActiveStatus(User user, boolean isActive) {
        if (isActive == user.getIsActive()) {
            String status = isActive ? "activate" : "deactivate";
            throw new ServiceException(format("Could not %s user: user is already %sed", status, status));
        }
    }
}

