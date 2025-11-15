package com.m01project.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRequestDto {
    private String email;
    private String password;
    private String phone;
}
