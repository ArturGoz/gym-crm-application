package com.gca.service;

import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.model.User;

public interface UserService {
    User createUser(UserCreateRequest request);

    boolean isUserCredentialsValid(String username, String rawPassword);

    void changeUserPassword(PasswordChangeRequest passwordChangeRequest);

    void toggleActiveStatus(String username);

    User getUserById(Long id);
}
