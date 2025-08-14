package com.e_commerce.user_service.dto;

import com.e_commerce.user_service.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8)
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is Required")
    private String name;

    private Role role = Role.BUYER; // default role
}
