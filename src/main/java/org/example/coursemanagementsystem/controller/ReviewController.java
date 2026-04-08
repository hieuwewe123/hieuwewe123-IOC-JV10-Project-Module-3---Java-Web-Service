package org.example.coursemanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.ReviewCreateRequest;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.ReviewResponse;
import org.example.coursemanagementsystem.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/api/courses/{course_id}/reviews")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByCourse(@PathVariable Long course_id) {
        List<ReviewResponse> reviews = reviewService.getReviewsByCourse(course_id);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đánh giá thành công", reviews));
    }

    @PostMapping("/api/courses/{course_id}/reviews")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Long course_id,
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        request.setCourseId(course_id);
        ReviewResponse review = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Tạo đánh giá thành công", review));
    }

    @PutMapping("/api/reviews/{review_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long review_id,
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        ReviewResponse review = reviewService.updateReview(review_id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật đánh giá thành công", review));
    }

    @DeleteMapping("/api/reviews/{review_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long review_id) {
        reviewService.deleteReview(review_id);
        return ResponseEntity.ok(ApiResponse.success("Xóa đánh giá thành công", null));
    }
}
