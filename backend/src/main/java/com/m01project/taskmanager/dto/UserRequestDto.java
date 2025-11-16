package com.m01project.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequestDto {
    @NotNull(message = "email can not be null")
    @Email(message = "Email format is invalid")
    private String email;

    @NotNull(message = "password can not be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Pattern(
            regexp = "^\\+?[0-9]+$",
            message = "Phone must contain only digits and optional + sign"
    )
    private String phone;
}
