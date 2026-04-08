package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.CourseCreateRequest;
import org.example.coursemanagementsystem.dto.response.CourseResponse;
import org.example.coursemanagementsystem.entity.CourseStatus;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseCreateRequest request);
    CourseResponse getCourseById(Long courseId);
    List<CourseResponse> getAllCourses();
    List<CourseResponse> getCoursesByStatus(CourseStatus status);
    List<CourseResponse> getCoursesByTeacher(Long teacherId);
    List<CourseResponse> searchCourses(String keyword);
    CourseResponse updateCourse(Long courseId, CourseCreateRequest request);
    CourseResponse updateCourseStatus(Long courseId, CourseStatus status);
    void deleteCourse(Long courseId);
}
