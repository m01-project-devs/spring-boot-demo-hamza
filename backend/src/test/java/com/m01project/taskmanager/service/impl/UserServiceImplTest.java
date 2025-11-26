package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.dto.UserRequestDto;
import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_WhenUserDoNotExist() {
        UserRequestDto request = new UserRequestDto("user1@test.de", "1111", "user1");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("user1@test.de");
        savedUser.setPhone("1111");
        savedUser.setPassword("user1");

        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User created = userService.createUser(request);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("user1@test.de", created.getEmail());
        assertEquals("1111", created.getPhone());
    }

    @Test
    void createUser_WhenEmailAlreadyExists_ShouldThrowException() {
        UserRequestDto request = new UserRequestDto("user1@test.com", "1111", "");
        when(userRepository.existsByEmail("user1@test.com")).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(request));
        assertEquals("this user is already in db with email: user1@test.com",
                exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenUserExists() {
        UserRequestDto request = new UserRequestDto("user1@test.de", "newPass", "22222222");
        User existingUser = new User();
        existingUser.setEmail("user1@test.de");
        existingUser.setPassword("oldPass");
        existingUser.setPhone("1111111");

        when(userRepository.getUserByEmail("user1@test.de")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(request);
        assertEquals("22222222", updatedUser.getPhone());
        assertEquals("newPass", updatedUser.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_WhenUserDoNotExist_ShouldThrowException() {
        UserRequestDto request = new UserRequestDto("user1@test.com", "11111111", "22222222");
        when(userRepository.getUserByEmail("user1@test.com")).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(request));
        assertEquals("User can not be found for email: user1@test.com",
                exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetUserByEmail() {
        User user = new User(1L, "user1@test.de", "user1", "1111", null);
        when(userRepository.getUserByEmail("user1@test.de")).thenReturn(Optional.of(user));

        Optional<User> fetchedOpt = userService.getUserByEmail("user1@test.de");
        assertTrue(fetchedOpt.isPresent());
        User fetched = fetchedOpt.get();

        assertNotNull(fetched);
        assertEquals("user1@test.de", fetched.getEmail());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(
                new User(1L, "user1@test.de", "user1", "1111", null)));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    void deleteUser_WhenUserDoesntExist_ShouldReturnFalse() {
        String email = "notFound@test.com";
        when(userRepository.getUserByEmail(email)).thenReturn(Optional.empty());
        boolean result = userService.deleteUser(email);
        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnTrue() {
        String email = "user1@test.com";
        User user = new User();
        user.setEmail(email);
        user.setId(1L);

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));
        boolean result = userService.deleteUser(email);
        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }
}