package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.NotificationCreateRequest;
import org.example.coursemanagementsystem.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getCurrentUserNotifications();
    NotificationResponse markAsRead(Long notificationId);
    NotificationResponse createNotification(NotificationCreateRequest request);
    void deleteNotification(Long notificationId);
}
