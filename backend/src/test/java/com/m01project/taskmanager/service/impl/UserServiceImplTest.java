package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser() {
        User user = new User(1L, "user1@test.de", "user1", "1111", null);
        when(userRepository.save(user)).thenReturn(new User(1L, "user1@test.de", "user1", "1111", null));

        User created = userService.createUser(user);
        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("user1@test.de", created.getEmail());
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
}