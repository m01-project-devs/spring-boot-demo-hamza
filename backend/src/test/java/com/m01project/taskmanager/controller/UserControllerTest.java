package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.dto.UserRequestDto;
import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void testGetUser_UserExists() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPhone("1234567890");

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phone").value("1234567890"));
    }

    @Test
    void testGetUser_UserNotFound() throws Exception {
        when(userService.getUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/notfound@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setEmail("new@example.com");
        request.setPhone("1112223333");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("new@example.com");
        savedUser.setPhone("1112223333");

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/1"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void testUpdateUser_UserExists() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setEmail("update@example.com");
        request.setPhone("999888777");

        User existingUser = new User();
        existingUser.setEmail("update@example.com");
        existingUser.setPhone("111222333");

        when(userService.getUserByEmail("update@example.com")).thenReturn(Optional.of(existingUser));
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(existingUser);

        mockMvc.perform(put("/api/users/update@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"));
    }

    @Test
    void testDeleteUser_UserExists() throws Exception {
        when(userService.deleteUser("delete@example.com")).thenReturn(true);

        mockMvc.perform(delete("/api/users/delete@example.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_UserNotFound() throws Exception {
        when(userService.deleteUser("notfound@example.com")).thenReturn(false);

        mockMvc.perform(delete("/api/users/notfound@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidEmailReturns400() throws Exception {
        String json = """
                {
                    "email": "wrongemail",
                    "password": "password123",
                    "phone": "+905555555555"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
