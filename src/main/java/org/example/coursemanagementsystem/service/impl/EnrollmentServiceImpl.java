package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.EnrollmentCreateRequest;
import org.example.coursemanagementsystem.dto.response.EnrollmentResponse;
import org.example.coursemanagementsystem.entity.*;
import org.example.coursemanagementsystem.mapper.EnrollmentMapper;
import org.example.coursemanagementsystem.repository.*;
import org.example.coursemanagementsystem.service.EnrollmentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentMapper enrollmentMapper;

        private User getCurrentUser() {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                return userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        }

        private void ensureStudentOwnsEnrollment(Enrollment enrollment) {
                User currentUser = getCurrentUser();
                if (currentUser.getRole() == UserRole.STUDENT
                                && !enrollment.getStudent().getUserId().equals(currentUser.getUserId())) {
                        throw new IllegalArgumentException("Bạn không có quyền truy cập đăng ký này");
                }
        }

    @Override
    public EnrollmentResponse enrollCourse(EnrollmentCreateRequest request) {
                User currentUser = getCurrentUser();
                if (currentUser.getRole() != UserRole.STUDENT) {
                        throw new IllegalArgumentException("Chỉ sinh viên mới có thể đăng ký khóa học");
                }

                User student = currentUser;

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        // Kiểm tra xem sinh viên đã đăng ký khóa học này chưa
        if (enrollmentRepository.findByStudentAndCourse(student, course).isPresent()) {
            throw new IllegalArgumentException("Sinh viên đã đăng ký khóa học này");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ENROLLED)
                .progressPercentage(BigDecimal.ZERO)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentById(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Đăng ký không tồn tại"));
                ensureStudentOwnsEnrollment(enrollment);
        return enrollmentMapper.toResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getCurrentStudentEnrollments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại"));

        return enrollmentRepository.findByStudent(student)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại"));

        return enrollmentRepository.findByStudent(student)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        return enrollmentRepository.findByCourse(course)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    public EnrollmentResponse completeLessonInEnrollment(Long enrollmentId, Long lessonId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Đăng ký không tồn tại"));
                ensureStudentOwnsEnrollment(enrollment);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Bài học không tồn tại"));

        // Kiểm tra bài học thuộc khóa học mà sinh viên đăng ký
        if (!lesson.getCourse().getCourseId().equals(enrollment.getCourse().getCourseId())) {
            throw new IllegalArgumentException("Bài học không thuộc khóa học này");
        }

        // Tạo hoặc cập nhật LessonProgress
        LessonProgress progress = lessonProgressRepository
                .findByEnrollmentAndLesson(enrollment, lesson)
                .orElse(LessonProgress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .isCompleted(false)
                        .build());

        progress.setIsCompleted(true);
        lessonProgressRepository.save(progress);

        // Tính toán progress_percentage
        List<Lesson> allLessons = lessonRepository.findByCourseAndIsPublishedOrderByOrderIndex(enrollment.getCourse(), true);
        if (!allLessons.isEmpty()) {
            long completedCount = lessonProgressRepository.findByEnrollmentAndIsCompleted(enrollment, true).size();
            BigDecimal percentage = BigDecimal.valueOf(completedCount)
                    .divide(BigDecimal.valueOf(allLessons.size()), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            enrollment.setProgressPercentage(percentage);
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(updatedEnrollment);
    }

    @Override
    public void unenrollCourse(Long enrollmentId) {
                Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                                .orElseThrow(() -> new RuntimeException("Đăng ký không tồn tại"));
                ensureStudentOwnsEnrollment(enrollment);
                enrollmentRepository.delete(enrollment);
    }
}
