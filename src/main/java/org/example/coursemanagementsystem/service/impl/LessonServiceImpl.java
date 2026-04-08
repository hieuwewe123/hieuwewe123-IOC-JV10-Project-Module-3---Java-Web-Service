package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.LessonCreateRequest;
import org.example.coursemanagementsystem.dto.response.LessonResponse;
import org.example.coursemanagementsystem.entity.Course;
import org.example.coursemanagementsystem.entity.Lesson;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.entity.UserRole;
import org.example.coursemanagementsystem.mapper.LessonMapper;
import org.example.coursemanagementsystem.repository.CourseRepository;
import org.example.coursemanagementsystem.repository.LessonRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.LessonService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
        private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

        private void validateTeacherOwnership(Course course) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User currentUser = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                if (currentUser.getRole() == UserRole.TEACHER
                                && !course.getTeacher().getUserId().equals(currentUser.getUserId())) {
                        throw new IllegalArgumentException("Bạn không phải giảng viên phụ trách khóa học này");
                }
        }

    @Override
    public LessonResponse createLesson(LessonCreateRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));
                validateTeacherOwnership(course);

        Lesson lesson = Lesson.builder()
                .course(course)
                .title(request.getTitle())
                .contentUrl(request.getContentUrl())
                .textContent(request.getTextContent())
                .orderIndex(request.getOrderIndex())
                .isPublished(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toResponse(savedLesson);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Bài học không tồn tại"));
                if (!Boolean.TRUE.equals(lesson.getIsPublished())) {
                        throw new IllegalArgumentException("Bài học chưa được xuất bản");
                }
        return lessonMapper.toResponse(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        return lessonRepository.findByCourseOrderByOrderIndex(course)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getPublishedLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        return lessonRepository.findByCourseAndIsPublishedOrderByOrderIndex(course, true)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

        @Override
        @Transactional(readOnly = true)
        public String getLessonContentPreview(Long lessonId) {
                Lesson lesson = lessonRepository.findById(lessonId)
                                .orElseThrow(() -> new RuntimeException("Bài học không tồn tại"));

                String content = lesson.getTextContent();
                if (content == null || content.isBlank()) {
                        return "";
                }
                return content.length() <= 200 ? content : content.substring(0, 200) + "...";
        }

    @Override
    public LessonResponse updateLesson(Long lessonId, LessonCreateRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Bài học không tồn tại"));
        validateTeacherOwnership(lesson.getCourse());

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));
        validateTeacherOwnership(course);

        lesson.setCourse(course);
        lesson.setTitle(request.getTitle());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setTextContent(request.getTextContent());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setUpdatedAt(LocalDateTime.now());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updatedLesson);
    }

    @Override
    public LessonResponse publishLesson(Long lessonId, Boolean isPublished) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Bài học không tồn tại"));
                validateTeacherOwnership(lesson.getCourse());

        lesson.setIsPublished(isPublished);
        lesson.setUpdatedAt(LocalDateTime.now());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updatedLesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
                Lesson lesson = lessonRepository.findById(lessonId)
                                .orElseThrow(() -> new RuntimeException("Bài học không tồn tại"));
                validateTeacherOwnership(lesson.getCourse());
                lessonRepository.delete(lesson);
    }
}
