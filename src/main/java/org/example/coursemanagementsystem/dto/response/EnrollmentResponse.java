package org.example.coursemanagementsystem.dto.response;

import lombok.*;
import org.example.coursemanagementsystem.entity.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long enrollmentId;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private LocalDateTime completionDate;
    private BigDecimal progressPercentage;
}
