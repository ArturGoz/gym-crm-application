package com.gca.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreationHelper {
    private final UsernameGenerator usernameGenerator;
    private final RandomPasswordGenerator randomPasswordGenerator;
    private final CombinedUsernameExistenceChecker usernameExistenceChecker;

    public String generateUsername(String firstName, String lastName) {
        return usernameGenerator.generate(firstName, lastName, usernameExistenceChecker::exists);
    }

    public String generatePassword() {
        return randomPasswordGenerator.generatePassword();
    }
}
