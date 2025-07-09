package com.gca.service.impl;

import com.gca.GymTestProvider;
import com.gca.dao.UserDAO;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserResponse;
import com.gca.dto.user.UserUpdateRequest;
import com.gca.exception.ServiceException;
import com.gca.mapper.UserMapper;
import com.gca.model.User;
import com.gca.service.common.UserProfileService;
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
        UserResponse expected = GymTestProvider.constructUserResponse();

        when(userProfileService.generateUsername(request.getFirstName(), request.getLastName()))
                .thenReturn("john_doe");
        when(userProfileService.generatePassword()).thenReturn("securePass123");
        when(userMapper.toEntity(request)).thenReturn(mapped);
        when(userDAO.create(any(User.class))).thenReturn(saved);
        when(userMapper.toResponse(saved)).thenReturn(expected);

        UserResponse actual = userService.createUser(request);

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
        UserResponse expected = GymTestProvider.constructUserResponse();

        when(userDAO.getById(request.getId())).thenReturn(existing);
        when(userMapper.toEntity(request)).thenReturn(updatedEntity);
        when(userDAO.update(any(User.class))).thenReturn(updatedEntity);
        when(userMapper.toResponse(updatedEntity)).thenReturn(expected);

        UserResponse actual = userService.updateUser(request);

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

        ServiceException ex = assertThrows(ServiceException.class, () -> userService.updateUser(request));

        assertEquals("User not found", ex.getMessage());
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
        UserResponse expected = GymTestProvider.constructUserResponse();

        when(userDAO.getById(1L)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(expected);

        UserResponse actual = userService.getUserById(1L);

        assertEquals(expected, actual);
        verify(userDAO).getById(1L);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userDAO.getById(1L)).thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class, () -> userService.getUserById(1L));

        assertEquals("User not found", ex.getMessage());
        verify(userDAO).getById(1L);
        verifyNoInteractions(userMapper);
    }

    @Test
    void isUserCredentialsValid_validCredentials_returnsTrue() {
        User user = GymTestProvider.constructUser();
        when(userDAO.getByUsername(user.getUsername())).thenReturn(user);

        boolean result = userService.isUserCredentialsValid(user.getUsername(), user.getPassword());

        assertTrue(result);
        verify(userDAO).getByUsername(user.getUsername());
    }

    @Test
    void isUserCredentialsValid_invalidPassword_returnsFalse() {
        User user = GymTestProvider.constructUser();
        when(userDAO.getByUsername(user.getUsername())).thenReturn(user);

        boolean result = userService.isUserCredentialsValid(user.getUsername(), "wrongPass");

        assertFalse(result);
        verify(userDAO).getByUsername(user.getUsername());
    }

    @Test
    void isUserCredentialsValid_userNotFound_returnsFalse() {
        when(userDAO.getByUsername("unknown")).thenReturn(null);

        boolean result = userService.isUserCredentialsValid("unknown", "any");

        assertFalse(result);
        verify(userDAO).getByUsername("unknown");
    }

    @Test
    void changeUserPassword_success() {
        User user = GymTestProvider.constructUser();
        when(userDAO.getById(user.getId())).thenReturn(user);

        userService.changeUserPassword(user.getId(), "newPassword123");

        assertEquals("newPassword123", user.getPassword());
        verify(userDAO).getById(user.getId());
        verify(userDAO).update(user);
    }

    @Test
    void changeUserPassword_userNotFound_throwsException() {
        when(userDAO.getById(1L)).thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class, () ->
                userService.changeUserPassword(1L, "newPass"));

        assertEquals("User not found", ex.getMessage());
        verify(userDAO).getById(1L);
    }
}
