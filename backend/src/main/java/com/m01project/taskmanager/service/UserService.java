package com.m01project.taskmanager.service;

import com.m01project.taskmanager.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserByEmail(String email);
    List<User> getAllUsers();
}
