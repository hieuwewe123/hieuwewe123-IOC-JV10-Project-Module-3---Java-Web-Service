package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.entity.Course;
import org.example.coursemanagementsystem.entity.Enrollment;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.repository.CourseRepository;
import org.example.coursemanagementsystem.repository.EnrollmentRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Map<String, Object>> getTopCourses() {
        List<Course> courses = courseRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Course course : courses) {
            Map<String, Object> item = new HashMap<>();
            item.put("courseId", course.getCourseId());
            item.put("title", course.getTitle());
            item.put("teacher", course.getTeacher().getFullName());
            item.put("enrollmentCount", course.getEnrollments() != null ? course.getEnrollments().size() : 0);
            result.add(item);
        }

        result.sort((a, b) -> Integer.compare((Integer) b.get("enrollmentCount"), (Integer) a.get("enrollmentCount")));
        return result;
    }

    @Override
    public Map<String, Object> getStudentProgress(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", student.getUserId());
        result.put("studentName", student.getFullName());
        result.put("totalEnrollments", enrollments.size());

        List<Map<String, Object>> details = new ArrayList<>();
        BigDecimal totalProgress = BigDecimal.ZERO;

        for (Enrollment enrollment : enrollments) {
            Map<String, Object> item = new HashMap<>();
            item.put("enrollmentId", enrollment.getEnrollmentId());
            item.put("courseId", enrollment.getCourse().getCourseId());
            item.put("courseTitle", enrollment.getCourse().getTitle());
            item.put("status", enrollment.getStatus().name());
            item.put("progressPercentage", enrollment.getProgressPercentage());
            details.add(item);
            if (enrollment.getProgressPercentage() != null) {
                totalProgress = totalProgress.add(enrollment.getProgressPercentage());
            }
        }

        result.put("enrollments", details);
        result.put("averageProgress", enrollments.isEmpty() ? BigDecimal.ZERO : totalProgress.divide(BigDecimal.valueOf(enrollments.size()), 2, java.math.RoundingMode.HALF_UP));
        return result;
    }

    @Override
    public Map<String, Object> getTeacherCoursesOverview(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Giáo viên không tồn tại"));

        List<Course> courses = courseRepository.findByTeacher(teacher);

        Map<String, Object> result = new HashMap<>();
        result.put("teacherId", teacher.getUserId());
        result.put("teacherName", teacher.getFullName());
        result.put("totalCourses", courses.size());

        int totalEnrollments = 0;
        List<Map<String, Object>> courseSummaries = new ArrayList<>();

        for (Course course : courses) {
            int enrollmentCount = course.getEnrollments() != null ? course.getEnrollments().size() : 0;
            totalEnrollments += enrollmentCount;

            Map<String, Object> item = new HashMap<>();
            item.put("courseId", course.getCourseId());
            item.put("title", course.getTitle());
            item.put("status", course.getStatus().name());
            item.put("enrollmentCount", enrollmentCount);
            courseSummaries.add(item);
        }

        result.put("totalEnrollments", totalEnrollments);
        result.put("courses", courseSummaries);
        return result;
    }
}
