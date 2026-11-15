package com.example.auth_service.dto.response;

import lombok.Data;

@Data
public class AuthResponse {

    private String username;
    private String token;
}
