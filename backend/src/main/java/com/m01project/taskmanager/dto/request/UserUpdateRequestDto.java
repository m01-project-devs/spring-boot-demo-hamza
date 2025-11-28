package com.m01project.taskmanager.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequestDto {

    @NotNull(message = "password can not be null.")
    @Size(min = 8, max = 16)
    private String password;

    @Pattern(
            regexp = "^\\+?[0-9]+$",
            message = "Phone must contain only digits and optional + sign"
    )
    private String phone;
}
