package com.m01project.taskmanager.service.impl;

import com.m01project.taskmanager.dto.UserRequestDto;
import com.m01project.taskmanager.model.User;
import com.m01project.taskmanager.repository.UserRepository;
import com.m01project.taskmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;
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
        String email = request.getEmail();
        boolean exists = userRepository.existsByEmail(email);
        if(exists) {
            throw new IllegalArgumentException("this user is already in db with email: " + email);
        }
        User user = new User();
        user.setEmail(email);
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserRequestDto request) {
        Optional<User> user = userRepository.getUserByEmail(request.getEmail());
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User can not be found for email: " + request.getEmail());
        }
        User updatedUser = user.get();
        updatedUser.setPhone(request.getPhone());
        updatedUser.setPassword(request.getPassword());
        return userRepository.save(updatedUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

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
