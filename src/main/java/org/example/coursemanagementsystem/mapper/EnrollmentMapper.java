package org.example.coursemanagementsystem.mapper;

import org.example.coursemanagementsystem.dto.response.EnrollmentResponse;
import org.example.coursemanagementsystem.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        if (enrollment == null) return null;

        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .studentId(enrollment.getStudent().getUserId())
                .studentName(enrollment.getStudent().getFullName())
                .courseId(enrollment.getCourse().getCourseId())
                .courseName(enrollment.getCourse().getTitle())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .completionDate(enrollment.getCompletionDate())
                .progressPercentage(enrollment.getProgressPercentage())
                .build();
    }
}
