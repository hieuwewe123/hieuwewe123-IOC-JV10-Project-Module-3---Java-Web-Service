package org.example.coursemanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCreateRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long teacherId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    private Integer durationHours;
}
