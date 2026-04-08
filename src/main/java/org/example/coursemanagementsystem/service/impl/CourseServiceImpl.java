package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.CourseCreateRequest;
import org.example.coursemanagementsystem.dto.response.CourseResponse;
import org.example.coursemanagementsystem.dto.response.LessonResponse;
import org.example.coursemanagementsystem.entity.Course;
import org.example.coursemanagementsystem.entity.CourseStatus;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.mapper.CourseMapper;
import org.example.coursemanagementsystem.mapper.LessonMapper;
import org.example.coursemanagementsystem.repository.CourseRepository;
import org.example.coursemanagementsystem.repository.LessonRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.CourseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;

    @Override
    public CourseResponse createCourse(CourseCreateRequest request) {
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Giáo viên không tồn tại"));

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .price(request.getPrice())
                .durationHours(request.getDurationHours())
                .status(CourseStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (currentUser.getRole() != org.example.coursemanagementsystem.entity.UserRole.ADMIN
            && course.getStatus() != CourseStatus.PUBLISHED) {
            throw new IllegalArgumentException("Bạn không có quyền xem khóa học này");
        }

        CourseResponse response = courseMapper.toResponse(course);
        List<LessonResponse> publishedLessons = lessonRepository
                .findByCourseAndIsPublishedOrderByOrderIndex(course, true)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
        response.setLessons(publishedLessons);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        List<Course> courses = currentUser.getRole() == org.example.coursemanagementsystem.entity.UserRole.ADMIN
            ? courseRepository.findAll()
            : courseRepository.findByStatus(CourseStatus.PUBLISHED);

        return courses
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByStatus(CourseStatus status) {
        return courseRepository.findByStatus(status)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Giáo viên không tồn tại"));

        return courseRepository.findByTeacher(teacher)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> searchCourses(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public CourseResponse updateCourse(Long courseId, CourseCreateRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Giáo viên không tồn tại"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);
        course.setPrice(request.getPrice());
        course.setDurationHours(request.getDurationHours());
        course.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    public CourseResponse updateCourseStatus(Long courseId, CourseStatus status) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        course.setStatus(status);
        course.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Khóa học không tồn tại");
        }
        courseRepository.deleteById(courseId);
    }
}
