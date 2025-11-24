package com.m01project.taskmanager.service;

import com.m01project.taskmanager.dto.UserRequestDto;
import com.m01project.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(UserRequestDto request);
    User updateUser(UserRequestDto request);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    boolean deleteUser(String email);
}
