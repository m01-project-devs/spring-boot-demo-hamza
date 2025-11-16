package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.dto.UserRequestDto;
import com.m01project.taskmanager.dto.UserResponseDto;
import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> new UserResponseDto(user.getEmail(), user.getPhone()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto request) {

        User saved = userService.createUser(request);
        UserResponseDto response = new UserResponseDto(saved.getEmail(), saved.getPhone());
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(response);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String email, @RequestBody UserRequestDto request) {
        Optional<User> existing = userService.getUserByEmail(email);
        if(existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User updated = userService.createUser(request);

        UserResponseDto response = new UserResponseDto(updated.getEmail(), updated.getPhone());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUser(email);
        if(!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
