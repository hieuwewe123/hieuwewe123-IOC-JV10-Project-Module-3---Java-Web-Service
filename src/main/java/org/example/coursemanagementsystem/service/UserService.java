package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.UserCreateRequest;
import org.example.coursemanagementsystem.dto.response.UserResponse;
import org.example.coursemanagementsystem.entity.UserRole;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUserById(Long userId);
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersByRole(UserRole role);
    List<UserResponse> getUsersByStatus(Boolean isActive);
    UserResponse updateUser(Long userId, UserCreateRequest request);
    UserResponse updateUserRole(Long userId, UserRole role);
    UserResponse updateUserStatus(Long userId, Boolean isActive);
    void deleteUser(Long userId);
    UserResponse updateUserProfile(Long userId, UserCreateRequest request);
    void changePassword(Long userId, String oldPassword, String newPassword);
}
