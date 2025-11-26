package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.dto.UserRequestDto;
import com.m01project.taskmanager.dto.UserResponseDto;
import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable @Email @NotBlank String email) {
        return userService.getUserByEmail(email)
                .map(user -> new UserResponseDto(user.getEmail(), user.getPhone()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        User saved = userService.createUser(request);
        UserResponseDto response = getResponse(saved);
        URI location = URI.create("/api/users/" + saved.getEmail());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDto> updateUser
            (@PathVariable @Email @NotBlank String email,
             @Valid @RequestBody UserRequestDto request) {
        User updated = userService.updateUser(request);
        UserResponseDto response = getResponse(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Email @NotBlank String email) {
        boolean deleted = userService.deleteUser(email);
        if(!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private UserResponseDto getResponse(User user) {
        return new UserResponseDto(user.getEmail(), user.getPhone());
    }
}
