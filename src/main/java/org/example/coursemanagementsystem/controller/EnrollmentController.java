package org.example.coursemanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.EnrollmentCreateRequest;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.EnrollmentResponse;
import org.example.coursemanagementsystem.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollCourse(@Valid @RequestBody EnrollmentCreateRequest request) {
        EnrollmentResponse enrollment = enrollmentService.enrollCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký khóa học thành công", enrollment));
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyEnrollments() {
        List<EnrollmentResponse> enrollments = enrollmentService.getCurrentStudentEnrollments();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách khóa học đã đăng ký thành công", enrollments));
    }

    @GetMapping(params = "student_id")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getEnrollmentsByStudent(@RequestParam Long student_id) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByStudent(student_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đăng ký thành công", enrollments));
    }

    @GetMapping(params = "course_id")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getEnrollmentsByCourse(@RequestParam Long course_id) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByCourse(course_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đăng ký theo khóa học thành công", enrollments));
    }

    @GetMapping("/{enrollment_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> getEnrollmentById(@PathVariable Long enrollment_id) {
        EnrollmentResponse enrollment = enrollmentService.getEnrollmentById(enrollment_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đăng ký thành công", enrollment));
    }

    @PutMapping("/{enrollment_id}/complete_lesson/{lesson_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> completeLessonInEnrollment(
            @PathVariable Long enrollment_id,
            @PathVariable Long lesson_id
    ) {
        EnrollmentResponse enrollment = enrollmentService.completeLessonInEnrollment(enrollment_id, lesson_id);
        return ResponseEntity.ok(ApiResponse.success("Đánh dấu bài học hoàn thành thành công", enrollment));
    }

    @DeleteMapping("/{enrollment_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Void>> unenrollCourse(@PathVariable Long enrollment_id) {
        enrollmentService.unenrollCourse(enrollment_id);
        return ResponseEntity.ok(ApiResponse.success("Hủy đăng ký khóa học thành công", null));
    }
}
