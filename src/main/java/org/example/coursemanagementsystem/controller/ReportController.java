package org.example.coursemanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/top_courses")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopCourses() {
        return ResponseEntity.ok(ApiResponse.success("Lấy thống kê khóa học phổ biến thành công", reportService.getTopCourses()));
    }

    @GetMapping("/student_progress/{student_id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentProgress(@PathVariable Long student_id) {
        return ResponseEntity.ok(ApiResponse.success("Lấy thống kê tiến độ sinh viên thành công", reportService.getStudentProgress(student_id)));
    }

    @GetMapping("/teacher_courses_overview/{teacher_id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTeacherCoursesOverview(@PathVariable Long teacher_id) {
        return ResponseEntity.ok(ApiResponse.success("Lấy tổng quan khóa học giảng viên thành công", reportService.getTeacherCoursesOverview(teacher_id)));
    }
}
