package com.m01project.taskmanager.service;

import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(UserCreateRequestDto request);
    User updateUser(String email, UserUpdateRequestDto request);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    Page<User> getUsers(Pageable pageable);
    boolean deleteUser(String email);
}
