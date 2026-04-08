package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.EnrollmentCreateRequest;
import org.example.coursemanagementsystem.dto.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enrollCourse(EnrollmentCreateRequest request);
    EnrollmentResponse getEnrollmentById(Long enrollmentId);
    List<EnrollmentResponse> getCurrentStudentEnrollments();
    List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId);
    List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId);
    EnrollmentResponse completeLessonInEnrollment(Long enrollmentId, Long lessonId);
    void unenrollCourse(Long enrollmentId);
}
