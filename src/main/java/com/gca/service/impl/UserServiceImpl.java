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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public UserResponse createUser(UserCreateRequest request) {
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
    public UserResponse updateUser(UserUpdateRequest request) {
        logger.debug("Updating user with id: {}", request.getId());

        User existing = userDAO.getById(request.getId());
        if (existing == null) {
            throw new ServiceException("User not found");
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
        userDAO.delete(id);
        logger.info("Deleted user with id: {}", id);
    }

    @Override
    public boolean isUserCredentialsValid(String username, String rawPassword) {
        User user = userDAO.getByUsername(username);
        if (user == null) return false;
        return user.getPassword().equals(rawPassword);
    }

    @Override
    public void changeUserPassword(Long userId, String newPassword) {
        User user = userDAO.getById(userId);
        if (user == null) throw new ServiceException("User not found");

        user.setPassword(newPassword);
        userDAO.update(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        logger.debug("Retrieving user with id: {}", id);
        User user = userDAO.getById(id);

        if (user == null) {
            throw new ServiceException("User not found");
        }

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        logger.debug("Fetching all users");
        List<String> usernames = userDAO.getAllUsernames();

        return usernames.stream()
                .map(username -> userDAO.getByUsername(username))
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}
