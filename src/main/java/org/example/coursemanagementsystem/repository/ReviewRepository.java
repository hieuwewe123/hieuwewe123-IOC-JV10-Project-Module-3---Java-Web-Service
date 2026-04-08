package org.example.coursemanagementsystem.repository;

import org.example.coursemanagementsystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourse_CourseId(Long courseId);
}
