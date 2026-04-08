package org.example.coursemanagementsystem.repository;

import org.example.coursemanagementsystem.entity.Enrollment;
import org.example.coursemanagementsystem.entity.Lesson;
import org.example.coursemanagementsystem.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByEnrollment_EnrollmentIdAndLesson_LessonId(Long enrollmentId, Long lessonId);
    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
    List<LessonProgress> findByEnrollmentAndIsCompleted(Enrollment enrollment, Boolean isCompleted);
}