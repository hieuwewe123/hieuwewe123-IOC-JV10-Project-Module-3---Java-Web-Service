package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequest {
    @NotNull
    private Long userId;

    @NotBlank
    private String message;

    private String type;

    private String targetUrl;
}
