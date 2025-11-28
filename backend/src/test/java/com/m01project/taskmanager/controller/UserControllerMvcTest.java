package com.m01project.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getUser_WhenUserExists() throws Exception {
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
    void getUser_WhenUserDoesntExist_ReturnUserNotFound() throws Exception {
        when(userService.getUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/notfound@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_WhenValidRequest_ShouldReturnCreatedUser() throws Exception {
        UserCreateRequestDto request = new UserCreateRequestDto();
        request.setEmail("new@example.com");
        request.setPhone("1112223333");
        request.setPassword("11111111");

        User savedUser = new User();
        savedUser.setEmail("new@example.com");
        savedUser.setPhone("1112223333");

        when(userService.createUser(any(UserCreateRequestDto.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/new@example.com"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.phone").value("1112223333"));
    }

    @Test
    void createUser_WhenEmailIsInvalid_ShouldReturnBadRequest400() throws Exception {
        String json = """
                {
                    "email": "wrongemail",
                    "password": "password1234",
                    "phone": "+900000120"
                }
                """;
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WhenPasswordIsInvalid_ShouldReturnBadRequest() throws Exception {
        String json = """
                {
                    "email": "user1@test.com",
                    "password": "1234",
                    "phone": "1111"
                }
                """;
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WhenUserExists() throws Exception {
        String email = "update@example.com";
        UserCreateRequestDto request = new UserCreateRequestDto();
        request.setPassword("11111111");
        request.setPhone("11111111");

        User existingUser = new User();
        existingUser.setEmail("update@example.com");
        existingUser.setPassword("22222222");
        existingUser.setPhone("22222222");

        when(userService.updateUser(email, any(UserUpdateRequestDto.class))).thenReturn(existingUser);

        mockMvc.perform(put("/api/users/update@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"))
                .andExpect(jsonPath("$.phone").value("22222222"));
    }

    @Test
    void deleteUser_WhenUserExists() throws Exception {
        when(userService.deleteUser("delete@example.com")).thenReturn(true);

        mockMvc.perform(delete("/api/users/delete@example.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_WhenUserDoesntExist_ShouldReturnUserNotFound() throws Exception {
        when(userService.deleteUser("notfound@example.com")).thenReturn(false);

        mockMvc.perform(delete("/api/users/notfound@example.com"))
                .andExpect(status().isNotFound());
    }
}
