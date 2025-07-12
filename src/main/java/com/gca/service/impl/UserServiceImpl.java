package com.gca.service.impl;

import com.gca.dao.UserDAO;
import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserResponse;
import com.gca.dto.user.UserUpdateRequest;
import com.gca.exception.ServiceException;
import com.gca.mapper.UserMapper;
import com.gca.model.User;
import com.gca.security.MyTransactional;
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
    private UserMapper userMapper;
    private UserProfileService userProfileService;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @MyTransactional
    @Override
    public UserResponse createUser(@Valid UserCreateRequest request) {
        logger.debug("Creating user for {} {}", request.getFirstName(), request.getLastName());

        String username = userProfileService.generateUsername(request.getFirstName(), request.getLastName());
        String password = userProfileService.generatePassword();

        User user = userMapper.toEntity(request).toBuilder()
                .username(username)
                .password(password)
                .isActive(true)
                .build();

        User created = userDAO.create(user);

        logger.info("Created user: {}", created);
        return userMapper.toResponse(created);
    }

    @MyTransactional
    @Override
    public UserResponse updateUser(@Valid UserUpdateRequest request) {
        logger.debug("Updating user with ID: {}", request.getId());

        User existing = Optional.ofNullable(userDAO.getById(request.getId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User with ID %d not found", request.getId())
                ));

        User updatedEntity = userMapper.toEntity(request).toBuilder()
                .id(existing.getId())
                .build();

        User updated = userDAO.update(updatedEntity);

        logger.info("Updated user: {}", updated);
        return userMapper.toResponse(updated);
    }

    @MyTransactional
    @Override
    public void deleteUser(Long id) {
        logger.debug("Deleting user with ID: {}", id);

        Optional.ofNullable(id)
                .orElseThrow(() -> new ServiceException("User ID must not be null"));

        userDAO.delete(id);

        logger.info("Deleted user with ID: {}", id);
    }

    @MyTransactional(readOnly = true)
    @Override
    public boolean isUserCredentialsValid(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return false;
        }

        return Optional.ofNullable(userDAO.findByUsername(username))
                .map(user -> userProfileService.verifyPassword(rawPassword, user.getPassword()))
                .orElse(false);
    }

    @MyTransactional
    @Override
    public void changeUserPassword(@Valid PasswordChangeRequest passwordChangeRequest) {
        Long userId = passwordChangeRequest.getUserId();
        logger.debug("Changing password for user with ID: {}", userId);

        User user = Optional.ofNullable(userDAO.getById(userId))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User with ID %d not found", userId)
                ));

        user.setPassword(userProfileService.encryptPassword(passwordChangeRequest.getPassword()));
        userDAO.update(user);

        logger.info("Changed password for user with ID: {}", userId);
    }

    @MyTransactional
    @Override
    public UserResponse toggleActiveStatus(String username) {
        logger.debug("Toggling active status for user with username: {}", username);

        User user = Optional.ofNullable(userDAO.findByUsername(username))
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with username %s not found", username)
                ));

        user.setIsActive(!user.getIsActive());
        User updatedUser = userDAO.update(user);

        logger.info("Toggled active status for user with username: {} to {}", username, updatedUser.getIsActive());

        return userMapper.toResponse(updatedUser);
    }

    @MyTransactional(readOnly = true)
    @Override
    public UserResponse getUserById(Long id) {
        logger.debug("Retrieving user with ID: {}", id);

        User user = Optional.ofNullable(userDAO.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User with ID %d not found", id)
                ));

        return userMapper.toResponse(user);
    }

    @MyTransactional(readOnly = true)
    @Override
    public UserResponse getUserByUsername(String username) {
        logger.debug("Retrieving user with username: {}", username);

        User user = Optional.ofNullable(userDAO.findByUsername(username))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User with username %s not found", username)
                ));

        return userMapper.toResponse(user);
    }
}

