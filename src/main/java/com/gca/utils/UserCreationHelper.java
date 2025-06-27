package com.gca.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreationHelper {
    private final UsernameGeneratorImpl usernameGeneratorImpl;
    private final RandomPasswordGenerator randomPasswordGenerator;
    private final CombinedUsernameExistenceChecker usernameExistenceChecker;

    public String generateUsername(String firstName, String lastName) {
        return usernameGeneratorImpl.generate(firstName, lastName, usernameExistenceChecker::exists);
    }

    public String generatePassword() {
        return randomPasswordGenerator.generatePassword();
    }
}
