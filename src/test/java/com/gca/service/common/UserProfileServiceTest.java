package com.gca.service.common;

import com.gca.service.helper.RandomPasswordGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UsernameGenerator usernameGenerator;
    @Mock
    private RandomPasswordGenerator randomPasswordGenerator;

    @InjectMocks
    private UserProfileService service;

    @Test
    void generateUsername_delegatesToUsernameGenerator() {
        when(usernameGenerator.generate("John", "Doe")).thenReturn("John.Doe");

        String username = service.generateUsername("John", "Doe");

        assertEquals("John.Doe", username);
        verify(usernameGenerator).generate("John", "Doe");
    }

    @Test
    void generatePassword_delegatesToRandomPasswordGenerator() {
        when(randomPasswordGenerator.generatePassword()).thenReturn("aB12cD34eF");

        String password = service.generatePassword();

        assertEquals("aB12cD34eF", password);
        verify(randomPasswordGenerator).generatePassword();
    }
}