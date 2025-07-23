package com.gca.service;

import com.gca.dto.PasswordChangeDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.model.User;

public interface UserService {
    User createUser(UserCreateDTO request);

    boolean isUserCredentialsValid(String username, String rawPassword);

    void changeUserPassword(PasswordChangeDTO passwordChangeDTO);

    void toggleActiveStatus(String username);

    User getUserById(Long id);
}
