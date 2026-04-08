package org.example.coursemanagementsystem.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> getTopCourses();
    Map<String, Object> getStudentProgress(Long studentId);
    Map<String, Object> getTeacherCoursesOverview(Long teacherId);
}
