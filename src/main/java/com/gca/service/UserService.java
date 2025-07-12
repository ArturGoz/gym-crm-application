package com.gca.service;

import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserResponse;
import com.gca.dto.user.UserUpdateRequest;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);

    UserResponse updateUser(UserUpdateRequest request);

    UserResponse getUserById(Long id);

    UserResponse getUserByUsername(String username);

    void deleteUser(Long id);

    boolean isUserCredentialsValid(String username, String password);

    void changeUserPassword(PasswordChangeRequest request);

    UserResponse toggleActiveStatus(String username);
}
