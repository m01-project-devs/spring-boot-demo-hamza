package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;   // mocked

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        UserController userController = new UserController(userService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void shouldReturnUser_WhenExists() throws Exception {

        // test user
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setPassword("1111");
        user.setPhone("55555");
        user.setCreatedAt(LocalDateTime.now());

        Mockito.when(userService.getUserById(1L))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.phone").value("55555"));
    }

    @Test
    void shouldReturn404_WhenUserNotFound() throws Exception {
        Mockito.when(userService.getUserById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}
