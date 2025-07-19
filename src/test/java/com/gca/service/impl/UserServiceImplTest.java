package com.gca.service.impl;

import com.gca.utils.GymTestProvider;
import com.gca.dao.UserDAO;
import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.mapper.UserMapper;
import com.gca.model.User;
import com.gca.service.common.UserProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO dao;

    @Mock
    private UserProfileService profileService;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void createUser_success() {
        UserCreateRequest request = GymTestProvider.createUserCreateRequest();
        User saved = GymTestProvider.constructUser();

        when(profileService.generateUsername(request.getFirstName(), request.getLastName()))
                .thenReturn("john_doe");
        when(profileService.generatePassword()).thenReturn("securePass123");
        when(mapper.toEntity(request)).thenReturn(saved);

        User actual = service.createUser(request);

        assertEquals(saved, actual);
        verify(profileService).generateUsername(request.getFirstName(), request.getLastName());
        verify(profileService).generatePassword();
    }

    @Test
    void getUserById_success() {
        User user = GymTestProvider.constructUser();

        when(dao.getById(1L)).thenReturn(user);

        User actual = service.getUserById(1L);

        assertEquals(user, actual);
        verify(dao).getById(1L);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(dao.getById(1L)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.getUserById(1L));

        assertEquals("User with ID 1 not found", ex.getMessage());
        verify(dao).getById(1L);
    }

    @Test
    void isUserCredentialsValid_validCredentials_returnsTrue() {
        User user = GymTestProvider.constructUser();

        when(dao.findByUsername(user.getUsername())).thenReturn(user);
        when(profileService.verifyPassword(any(String.class), any(String.class))).thenReturn(true);

        boolean result = service.isUserCredentialsValid(user.getUsername(), user.getPassword());

        assertTrue(result);
        verify(dao).findByUsername(user.getUsername());
    }

    @Test
    void isUserCredentialsValid_invalidPassword_returnsFalse() {
        User user = GymTestProvider.constructUser();

        when(dao.findByUsername(user.getUsername())).thenReturn(user);

        boolean result = service.isUserCredentialsValid(user.getUsername(), "wrongPass");

        assertFalse(result);
        verify(dao).findByUsername(user.getUsername());
    }

    @Test
    void changeUserPassword_success() {
        User actual = GymTestProvider.constructUser();
        PasswordChangeRequest request
                = new PasswordChangeRequest(actual.getId(), "newPassword");

        when(dao.getById(actual.getId())).thenReturn(actual);
        when(profileService.encryptPassword(any(String.class))).thenReturn(request.getPassword());

        service.changeUserPassword(request);

        assertEquals(request.getPassword(), actual.getPassword());
        verify(dao).getById(actual.getId());
        verify(dao).update(actual);
    }

    @Test
    void changeUserPassword_userNotFound_throwsException() {
        PasswordChangeRequest request
                = new PasswordChangeRequest(1L, "newPass");

        when(dao.getById(1L)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.changeUserPassword(request));

        assertEquals("User with ID 1 not found", ex.getMessage());
        verify(dao).getById(1L);
    }

    @Test
    void toggleActiveStatus_success() {
        User expected = GymTestProvider.constructUser();
        User updated = expected.toBuilder().isActive(!expected.getIsActive()).build();

        when(dao.findByUsername(expected.getUsername())).thenReturn(expected);
        when(dao.update(any(User.class))).thenReturn(updated);

        service.toggleActiveStatus(expected.getUsername());

        verify(dao).update(any(User.class));
    }
}
