package com.example.auth_service.controller;

import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.RegisterRequest;
import com.example.auth_service.dto.response.AuthResponse;
import com.example.auth_service.dto.response.BaseResponse;
import com.example.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok("User registered successfully", authService.register(request)));
    }

    @PostMapping("/login")
    public BaseResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return BaseResponse.ok("Login success", response);
    }
}
