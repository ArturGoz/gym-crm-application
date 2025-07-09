package com.gca.service;

import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserResponse;
import com.gca.dto.user.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);

    UserResponse updateUser(UserUpdateRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    void deleteUser(Long id);

    boolean isUserCredentialsValid(String username, String password);

    void changeUserPassword(Long userId, String newPassword);
}
