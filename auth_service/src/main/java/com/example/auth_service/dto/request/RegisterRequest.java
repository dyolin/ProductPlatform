package com.example.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @ToString.Exclude
    private String password;
}
