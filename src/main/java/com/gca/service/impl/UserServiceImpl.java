package com.gca.service.impl;

import com.gca.dao.UserDAO;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserResponse;
import com.gca.dto.user.UserUpdateRequest;
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

    @Override
    public UserResponse createUser(@Valid UserCreateRequest request) {
        logger.debug("Creating user for {} {}", request.getFirstName(), request.getLastName());

        String username = userProfileService
                .generateUsername(request.getFirstName(), request.getLastName());

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

    @Override
    public UserResponse updateUser(@Valid UserUpdateRequest request) {
        logger.debug("Updating user with id: {}", request.getId());

        User existing = userDAO.getById(request.getId());

        if (existing == null) {
            throw new EntityNotFoundException("User with id " + request.getId() + " not found");
        }

        User updatedEntity = userMapper.toEntity(request).toBuilder()
                .id(existing.getId())
                .build();

        User updated = userDAO.update(updatedEntity);
        logger.info("Updated user: {}", updated);

        return userMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(Long id) {
        logger.debug("Deleting user with id: {}", id);

        if (id == null) {
            throw new ServiceException("Id is null");
        }

        userDAO.delete(id);
        logger.info("Deleted user with id: {}", id);
    }

    @Override
    public boolean isUserCredentialsValid(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return false;
        }

        User user = userDAO.getByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(rawPassword);
    }

    @Override
    public void changeUserPassword(Long userId, String newPassword) {
        logger.debug("Changing password for user with id: {}", userId);

        User user = userDAO.getById(userId);

        if (user == null) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        user.setPassword(newPassword);
        userDAO.update(user);

        logger.info("Changed password for user with id: {}", userId);
    }

    @Override
    public UserResponse getUserById(Long id) {
        logger.debug("Retrieving user with id: {}", id);

        User user = userDAO.getById(id);
        if (user == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        return userMapper.toResponse(user);
    }
}
