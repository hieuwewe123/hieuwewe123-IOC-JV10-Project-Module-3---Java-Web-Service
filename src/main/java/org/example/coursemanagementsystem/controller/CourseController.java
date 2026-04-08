package org.example.coursemanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.CourseCreateRequest;
import org.example.coursemanagementsystem.dto.request.LessonCreateRequest;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.CourseResponse;
import org.example.coursemanagementsystem.dto.response.LessonResponse;
import org.example.coursemanagementsystem.entity.CourseStatus;
import org.example.coursemanagementsystem.service.CourseService;
import org.example.coursemanagementsystem.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách khóa học thành công", courses));
    }

    @GetMapping("/{course_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long course_id) {
        CourseResponse course = courseService.getCourseById(course_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin khóa học thành công", course));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseResponse course = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Tạo khóa học thành công", course));
    }

    @PutMapping("/{course_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long course_id,
            @Valid @RequestBody CourseCreateRequest request
    ) {
        CourseResponse course = courseService.updateCourse(course_id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật khóa học thành công", course));
    }

    @PutMapping("/{course_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourseStatus(
            @PathVariable Long course_id,
            @RequestBody Map<String, String> statusRequest
    ) {
        CourseStatus status = CourseStatus.valueOf(statusRequest.get("status"));
        CourseResponse course = courseService.updateCourseStatus(course_id, status);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái khóa học thành công", course));
    }

    @DeleteMapping("/{course_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long course_id) {
        courseService.deleteCourse(course_id);
        return ResponseEntity.ok(ApiResponse.success("Xóa khóa học thành công", null));
    }

    @GetMapping("/{course_id}/lessons")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessonsByCourse(
            @PathVariable Long course_id
    ) {
        List<LessonResponse> lessons = lessonService.getPublishedLessonsByCourse(course_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách bài học thành công", lessons));
    }

    @PostMapping("/{course_id}/lessons")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> createLessonForCourse(
            @PathVariable Long course_id,
            @Valid @RequestBody LessonCreateRequest request
    ) {
        request.setCourseId(course_id);
        LessonResponse lesson = lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Tạo bài học thành công", lesson));
    }

    @GetMapping(params = "search")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> searchCourses(@RequestParam String search) {
        List<CourseResponse> courses = courseService.searchCourses(search);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm khóa học thành công", courses));
    }

    @GetMapping(params = "teacher_id")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCoursesByTeacher(@RequestParam Long teacher_id) {
        List<CourseResponse> courses = courseService.getCoursesByTeacher(teacher_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách khóa học theo giáo viên thành công", courses));
    }

    @GetMapping(params = "status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCoursesByStatus(@RequestParam CourseStatus status) {
        List<CourseResponse> courses = courseService.getCoursesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách khóa học theo trạng thái thành công", courses));
    }
}
