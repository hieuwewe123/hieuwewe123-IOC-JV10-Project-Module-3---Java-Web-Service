package org.example.coursemanagementsystem.mapper;

import org.example.coursemanagementsystem.dto.response.CourseResponse;
import org.example.coursemanagementsystem.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseResponse toResponse(Course course) {
        if (course == null) return null;

        int enrollmentCount = course.getEnrollments() != null ? course.getEnrollments().size() : 0;

        return CourseResponse.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .teacherId(course.getTeacher().getUserId())
                .teacherName(course.getTeacher().getFullName())
                .price(course.getPrice())
                .durationHours(course.getDurationHours())
                .status(course.getStatus())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .enrollmentCount(enrollmentCount)
                .build();
    }
}
