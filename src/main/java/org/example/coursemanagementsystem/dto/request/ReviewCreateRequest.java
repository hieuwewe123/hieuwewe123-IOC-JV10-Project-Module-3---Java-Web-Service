package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateRequest {
    @NotNull
    private Long courseId;

    private Long studentId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
