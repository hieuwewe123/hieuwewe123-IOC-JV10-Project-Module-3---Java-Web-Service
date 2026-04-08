package org.example.coursemanagementsystem.repository;

import org.example.coursemanagementsystem.entity.Course;
import org.example.coursemanagementsystem.entity.CourseStatus;
import org.example.coursemanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByTeacher_UserId(Long teacherId);
    List<Course> findByTeacher(User teacher);
    List<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleKeyword, String descriptionKeyword);
}
