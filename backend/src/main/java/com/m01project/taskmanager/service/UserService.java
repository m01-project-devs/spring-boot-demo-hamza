package com.m01project.taskmanager.service;

import com.m01project.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void deleteUser(Long id);
}
