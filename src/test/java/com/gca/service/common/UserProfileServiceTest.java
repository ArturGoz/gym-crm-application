package com.gca.service.common;

import com.gca.service.helper.RandomPasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserProfileServiceTest {

    private UsernameGenerator usernameGenerator;
    private RandomPasswordGenerator randomPasswordGenerator;
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        usernameGenerator = mock(UsernameGenerator.class);
        randomPasswordGenerator = mock(RandomPasswordGenerator.class);
        userProfileService = new UserProfileService(usernameGenerator, randomPasswordGenerator);
    }

    @Test
    void generateUsername_delegatesToUsernameGenerator() {
        when(usernameGenerator.generate("John", "Doe")).thenReturn("John.Doe");

        String username = userProfileService.generateUsername("John", "Doe");

        assertEquals("John.Doe", username);
        verify(usernameGenerator).generate("John", "Doe");
    }

    @Test
    void generatePassword_delegatesToRandomPasswordGenerator() {
        when(randomPasswordGenerator.generatePassword()).thenReturn("aB12cD34eF");

        String password = userProfileService.generatePassword();

        assertEquals("aB12cD34eF", password);
        verify(randomPasswordGenerator).generatePassword();
    }
}