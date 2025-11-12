package com.m01project.taskmanager.repository;

import com.m01project.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User getUserByEmail(String email);
}
