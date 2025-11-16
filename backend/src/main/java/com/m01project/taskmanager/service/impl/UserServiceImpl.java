package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.dto.UserRequestDto;
import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserRequestDto request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
//
//    @Override
//    public Optional<User> getUserById(Long id) { return userRepository.findById(id); }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        if(user.isEmpty()) {
            return false;
        }
        userRepository.deleteById(user.get().getId());
        return true;
    }
}
