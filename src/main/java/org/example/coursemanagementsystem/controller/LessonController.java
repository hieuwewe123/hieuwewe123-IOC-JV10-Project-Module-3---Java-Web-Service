package org.example.coursemanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.LessonCreateRequest;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.LessonResponse;
import org.example.coursemanagementsystem.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/{lesson_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> getLessonById(@PathVariable Long lesson_id) {
        LessonResponse lesson = lessonService.getLessonById(lesson_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin bài học thành công", lesson));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(@Valid @RequestBody LessonCreateRequest request) {
        LessonResponse lesson = lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo bài học thành công", lesson));
    }

    @PutMapping("/{lesson_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(
            @PathVariable Long lesson_id,
            @Valid @RequestBody LessonCreateRequest request
    ) {
        LessonResponse lesson = lessonService.updateLesson(lesson_id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật bài học thành công", lesson));
    }

    @PutMapping("/{lesson_id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> publishLesson(
            @PathVariable Long lesson_id,
            @RequestBody Map<String, Boolean> publishRequest
    ) {
        Boolean isPublished = publishRequest.get("isPublished");
        LessonResponse lesson = lessonService.publishLesson(lesson_id, isPublished);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái bài học thành công", lesson));
    }

    @DeleteMapping("/{lesson_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable Long lesson_id) {
        lessonService.deleteLesson(lesson_id);
        return ResponseEntity.ok(ApiResponse.success("Xóa bài học thành công", null));
    }

    @GetMapping("/{lesson_id}/content_preview")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<Map<String, String>>> getContentPreview(@PathVariable Long lesson_id) {
        String preview = lessonService.getLessonContentPreview(lesson_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy nội dung xem trước thành công", Map.of("preview", preview)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessonsByCourse(
            @RequestParam(value = "course_id", required = false) Long courseId,
            @RequestParam(value = "publishedOnly", defaultValue = "false") boolean publishedOnly
    ) {
        List<LessonResponse> lessons;
        if (courseId != null) {
            if (publishedOnly) {
                lessons = lessonService.getPublishedLessonsByCourse(courseId);
            } else {
                lessons = lessonService.getLessonsByCourse(courseId);
            }
        } else {
            throw new IllegalArgumentException("course_id là bắt buộc");
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách bài học thành công", lessons));
    }
}
