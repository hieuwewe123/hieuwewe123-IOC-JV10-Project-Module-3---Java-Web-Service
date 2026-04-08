package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.ReviewCreateRequest;
import org.example.coursemanagementsystem.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(ReviewCreateRequest request);
    List<ReviewResponse> getReviewsByCourse(Long courseId);
    ReviewResponse updateReview(Long reviewId, ReviewCreateRequest request);
    void deleteReview(Long reviewId);
}
