package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.LessonCreateRequest;
import org.example.coursemanagementsystem.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {
    LessonResponse createLesson(LessonCreateRequest request);
    LessonResponse getLessonById(Long lessonId);
    List<LessonResponse> getLessonsByCourse(Long courseId);
    List<LessonResponse> getPublishedLessonsByCourse(Long courseId);
    String getLessonContentPreview(Long lessonId);
    LessonResponse updateLesson(Long lessonId, LessonCreateRequest request);
    LessonResponse publishLesson(Long lessonId, Boolean isPublished);
    void deleteLesson(Long lessonId);
}
