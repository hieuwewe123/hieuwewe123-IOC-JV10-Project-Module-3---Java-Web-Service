package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.ReviewCreateRequest;
import org.example.coursemanagementsystem.dto.response.ReviewResponse;
import org.example.coursemanagementsystem.entity.Course;
import org.example.coursemanagementsystem.entity.Review;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.entity.UserRole;
import org.example.coursemanagementsystem.mapper.ReviewMapper;
import org.example.coursemanagementsystem.repository.CourseRepository;
import org.example.coursemanagementsystem.repository.ReviewRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.ReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponse createReview(ReviewCreateRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        User student;
        if (request.getStudentId() != null) {
            student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại"));
        } else {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại"));
        }

        Review review = Review.builder()
                .course(course)
                .student(student)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByCourse(Long courseId) {
        return reviewRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public ReviewResponse updateReview(Long reviewId, ReviewCreateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        boolean isOwner = review.getStudent().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Bạn không có quyền cập nhật đánh giá này");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        boolean isOwner = review.getStudent().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Bạn không có quyền xóa đánh giá này");
        }

        reviewRepository.delete(review);
    }
}
