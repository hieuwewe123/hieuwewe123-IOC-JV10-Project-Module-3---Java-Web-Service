package org.example.coursemanagementsystem.repository;

import org.example.coursemanagementsystem.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserIdAndIsReadFalse(Long userId);
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}
