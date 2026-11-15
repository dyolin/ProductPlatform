package com.example.auth_service.service.impl;

import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.RegisterRequest;
import com.example.auth_service.dto.response.AuthResponse;
import com.example.auth_service.entity.User;
import com.example.auth_service.exception.DuplicateUsernameException;
import com.example.auth_service.exception.InvalidCredentialException;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "User " +request.getUsername()+ " has been registered successfully";
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialException(
                        "Invalid username or password"
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException(
                    "Invalid username or password"
            );
        }

        String token = jwtUtil.generateToken(user.getUsername());

        AuthResponse response = new AuthResponse();
        response.setUsername(user.getUsername());
        response.setToken(token);

        return response;
    }
}
