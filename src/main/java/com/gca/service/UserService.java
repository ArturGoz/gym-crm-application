package com.gca.service;

import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserDTO;
import com.gca.dto.user.UserUpdateRequest;

public interface UserService {
    UserDTO createUser(UserCreateRequest request);

    UserDTO updateUser(UserUpdateRequest request);

    UserDTO getUserById(Long id);

    UserDTO getUserByUsername(String username);

    void deleteUser(Long id);

    boolean isUserCredentialsValid(String username, String password);

    void changeUserPassword(PasswordChangeRequest request);

    UserDTO toggleActiveStatus(String username);
}
