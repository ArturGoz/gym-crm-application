package com.gca.service.common;

import com.gca.service.helper.RandomPasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileService {
    private final UsernameGenerator usernameGenerator;
    private final RandomPasswordGenerator randomPasswordGenerator;

    public String generateUsername(String firstName, String lastName) {
        return usernameGenerator.generate(firstName, lastName);
    }

    public String generatePassword() {
        return randomPasswordGenerator.generatePassword();
    }
}
