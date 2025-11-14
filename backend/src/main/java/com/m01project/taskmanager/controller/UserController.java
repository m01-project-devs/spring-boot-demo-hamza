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
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        User saved = userService.createUser(user);
        UserResponseDto response = new UserResponseDto(saved.getId(), saved.getEmail(), saved.getPhone());
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto request) {
        Optional<User> existing = userService.getUserById(id);
        if(existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = existing.get();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        User updated = userService.createUser(user);

        UserResponseDto response = new UserResponseDto(updated.getId(), updated.getEmail(), updated.getPhone());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable long id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
