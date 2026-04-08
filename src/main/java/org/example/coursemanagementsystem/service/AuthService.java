package org.example.coursemanagementsystem.service;
import org.example.coursemanagementsystem.dto.request.LoginRequest;
import org.example.coursemanagementsystem.dto.response.AuthResponse;
import org.example.coursemanagementsystem.dto.response.UserResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser();
    boolean verifyToken(String token);
    void logout(String token);
}
