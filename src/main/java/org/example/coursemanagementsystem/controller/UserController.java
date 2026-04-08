package org.example.coursemanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.UserCreateRequest;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.UserResponse;
import org.example.coursemanagementsystem.entity.UserRole;
import org.example.coursemanagementsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng thành công", users));
    }

    @GetMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long user_id) {
        UserResponse user = userService.getUserById(user_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công", user));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo người dùng thành công", user));
    }

    @PutMapping("/{user_id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long user_id,
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserResponse user = userService.updateUser(user_id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công", user));
    }

    @PutMapping("/{user_id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long user_id,
            @RequestBody Map<String, String> roleRequest
    ) {
        UserRole role = UserRole.valueOf(roleRequest.get("role"));
        UserResponse user = userService.updateUserRole(user_id, role);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật vai trò người dùng thành công", user));
    }

    @PutMapping("/{user_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long user_id,
            @RequestBody Map<String, Boolean> statusRequest
    ) {
        Boolean isActive = statusRequest.get("isActive");
        UserResponse user = userService.updateUserStatus(user_id, isActive);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái người dùng thành công", user));
    }

    @DeleteMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.ok(ApiResponse.success("Xóa người dùng thành công", null));
    }

    @PutMapping("/{user_id}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable Long user_id,
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserResponse user = userService.updateUserProfile(user_id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật hồ sơ thành công", user));
    }

    @PutMapping("/{user_id}/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long user_id,
            @RequestBody Map<String, String> passwordRequest
    ) {
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");
        userService.changePassword(user_id, oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công", null));
    }

    @GetMapping(params = "role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@RequestParam UserRole role) {
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng theo vai trò thành công", users));
    }

    @GetMapping(params = "status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByStatus(@RequestParam Boolean status) {
        List<UserResponse> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng theo trạng thái thành công", users));
    }
}
