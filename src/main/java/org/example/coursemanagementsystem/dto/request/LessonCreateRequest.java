package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonCreateRequest {
    @NotNull
    private Long courseId;

    @NotBlank
    private String title;

    private String contentUrl;

    private String textContent;

    @NotNull
    private Integer orderIndex;
}
