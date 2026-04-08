package org.example.coursemanagementsystem.repository;

import org.example.coursemanagementsystem.entity.Course;
import org.example.coursemanagementsystem.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourse_CourseIdAndIsPublishedTrue(Long courseId);
    List<Lesson> findByCourseOrderByOrderIndex(Course course);
    List<Lesson> findByCourseAndIsPublishedOrderByOrderIndex(Course course, Boolean isPublished);
}
