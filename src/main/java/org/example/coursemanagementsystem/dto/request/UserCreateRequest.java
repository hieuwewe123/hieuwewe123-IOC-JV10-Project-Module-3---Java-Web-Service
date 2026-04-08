package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.coursemanagementsystem.entity.UserRole;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, message = "Mật khẩu phải ít nhất 6 ký tự")
    private String password;

    @NotBlank
    private String fullName;

    private UserRole role = UserRole.STUDENT;
}
