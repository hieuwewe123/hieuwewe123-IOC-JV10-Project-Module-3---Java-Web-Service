package org.example.coursemanagementsystem.mapper;

import org.example.coursemanagementsystem.dto.response.ReviewResponse;
import org.example.coursemanagementsystem.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        if (review == null) return null;

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .courseId(review.getCourse().getCourseId())
                .courseTitle(review.getCourse().getTitle())
                .studentId(review.getStudent().getUserId())
                .studentName(review.getStudent().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
