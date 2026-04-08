package org.example.coursemanagementsystem.dto.response;

import lombok.*;
import org.example.coursemanagementsystem.entity.CourseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private Long courseId;
    private String title;
    private String description;
    private Long teacherId;
    private String teacherName;
    private BigDecimal price;
    private Integer durationHours;
    private CourseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer enrollmentCount;
    private List<LessonResponse> lessons;
}
