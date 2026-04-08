package org.example.coursemanagementsystem.dto.response;

import lombok.*;
import org.example.coursemanagementsystem.entity.UserRole;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
