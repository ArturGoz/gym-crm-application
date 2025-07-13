package com.gca.service.impl;

import com.gca.GymTestProvider;
import com.gca.dao.UserDAO;
import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserDTO;
import com.gca.dto.user.UserUpdateRequest;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success() {
        UserCreateRequest request = GymTestProvider.createUserCreateRequest();
        User mapped = GymTestProvider.constructUser().toBuilder().id(null).build();
        User saved = GymTestProvider.constructUser();
        UserDTO expected = GymTestProvider.constructUserResponse();

        when(userProfileService.generateUsername(request.getFirstName(), request.getLastName()))
                .thenReturn("john_doe");
        when(userProfileService.generatePassword()).thenReturn("securePass123");
        when(userMapper.toEntity(request)).thenReturn(mapped);
        when(userDAO.create(any(User.class))).thenReturn(saved);
        when(userMapper.toResponse(saved)).thenReturn(expected);

        UserDTO actual = userService.createUser(request);

        assertEquals(expected, actual);
        verify(userProfileService).generateUsername(request.getFirstName(), request.getLastName());
        verify(userProfileService).generatePassword();
        verify(userMapper).toEntity(request);
        verify(userDAO).create(any(User.class));
        verify(userMapper).toResponse(saved);
    }

    @Test
    void updateUser_success() {
        UserUpdateRequest request = GymTestProvider.createUserUpdateRequest();
        User existing = GymTestProvider.constructUser();
        User updatedEntity = existing.toBuilder().firstName("Updated").build();
        UserDTO expected = GymTestProvider.constructUserResponse();

        when(userDAO.getById(request.getId())).thenReturn(existing);
        when(userMapper.toEntity(request)).thenReturn(updatedEntity);
        when(userDAO.update(any(User.class))).thenReturn(updatedEntity);
        when(userMapper.toResponse(updatedEntity)).thenReturn(expected);

        UserDTO actual = userService.updateUser(request);

        assertEquals(expected, actual);
        verify(userDAO).getById(request.getId());
        verify(userMapper).toEntity(request);
        verify(userDAO).update(any(User.class));
        verify(userMapper).toResponse(updatedEntity);
    }

    @Test
    void updateUser_notFound_throwsException() {
        UserUpdateRequest request = GymTestProvider.createUserUpdateRequest();
        when(userDAO.getById(request.getId())).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.updateUser(request));

        assertEquals("User with ID 1 not found", ex.getMessage());
        verify(userDAO).getById(request.getId());
        verifyNoMoreInteractions(userMapper, userDAO);
    }

    @Test
    void deleteUser_success() {
        userService.deleteUser(1L);
        verify(userDAO).delete(1L);
    }

    @Test
    void getUserById_success() {
        User user = GymTestProvider.constructUser();
        UserDTO expected = GymTestProvider.constructUserResponse();

        when(userDAO.getById(1L)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(expected);

        UserDTO actual = userService.getUserById(1L);

        assertEquals(expected, actual);
        verify(userDAO).getById(1L);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userDAO.getById(1L)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));

        assertEquals("User with ID 1 not found", ex.getMessage());
        verify(userDAO).getById(1L);
        verifyNoInteractions(userMapper);
    }

    @Test
    void isUserCredentialsValid_validCredentials_returnsTrue() {
        User user = GymTestProvider.constructUser();

        when(userDAO.findByUsername(user.getUsername())).thenReturn(user);
        when(userProfileService.verifyPassword(any(String.class), any(String.class))).thenReturn(true);

        boolean result = userService.isUserCredentialsValid(user.getUsername(), user.getPassword());

        assertTrue(result);
        verify(userDAO).findByUsername(user.getUsername());
    }

    @Test
    void isUserCredentialsValid_invalidPassword_returnsFalse() {
        User user = GymTestProvider.constructUser();
        when(userDAO.findByUsername(user.getUsername())).thenReturn(user);

        boolean result = userService.isUserCredentialsValid(user.getUsername(), "wrongPass");

        assertFalse(result);
        verify(userDAO).findByUsername(user.getUsername());
    }

    @Test
    void isUserCredentialsValid_userNotFound_returnsFalse() {
        when(userDAO.findByUsername("unknown")).thenReturn(null);

        boolean result = userService.isUserCredentialsValid("unknown", "any");

        assertFalse(result);
        verify(userDAO).findByUsername("unknown");
    }

    @Test
    void changeUserPassword_success() {
        User actual = GymTestProvider.constructUser();
        PasswordChangeRequest request
                = new PasswordChangeRequest(actual.getId(), "newPassword");

        when(userDAO.getById(actual.getId())).thenReturn(actual);
        when(userProfileService.encryptPassword(any(String.class))).thenReturn(request.getPassword());

        userService.changeUserPassword(request);

        assertEquals(request.getPassword(), actual.getPassword());
        verify(userDAO).getById(actual.getId());
        verify(userDAO).update(actual);
    }

    @Test
    void changeUserPassword_userNotFound_throwsException() {
        PasswordChangeRequest request
                = new PasswordChangeRequest(1L, "newPass");

        when(userDAO.getById(1L)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                userService.changeUserPassword(request));

        assertEquals("User with ID 1 not found", ex.getMessage());
        verify(userDAO).getById(1L);
    }

    @Test
    void toggleActiveStatus_success() {
        User expected = GymTestProvider.constructUser();
        User updated = expected.toBuilder().isActive(!expected.getIsActive()).build();
        UserDTO expectedResponse = GymTestProvider.constructUserResponse().toBuilder()
                .isActive(updated.getIsActive())
                .build();

        when(userDAO.findByUsername(expected.getUsername())).thenReturn(expected);
        when(userDAO.update(any(User.class))).thenReturn(updated);
        when(userMapper.toResponse(updated)).thenReturn(expectedResponse);

        UserDTO actual = userService.toggleActiveStatus(expected.getUsername());

        assertEquals(expectedResponse, actual);
        assertEquals(expected.getIsActive(), actual.getIsActive());
        assertEquals(expected.getId(), actual.getId());
        verify(userDAO).update(any(User.class));
        verify(userMapper).toResponse(updated);
    }
}
