package com.citaa.citaa.service;

import com.citaa.citaa.model.Notification;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserService userService;

    public Notification createNotification(Notification notification) throws Exception {
        return notificationRepository.save(Notification.builder()
                .content(notification.getContent())
                .timestamp(LocalDateTime.now())
                .recipientId(notification.getRecipientId())
                .type(notification.getType())
                .isRead("UNREAD")
                .link(notification.getLink())
                .build());
    }

    public List<Notification> getNotificationsByUserId(int userId, String status) {
        return notificationRepository.findByRecipientIdAndIsRead(userId, status);
    }

    public Notification markAsRead(long notificationId, int userId) throws Exception {
        Notification noti = notificationRepository.findById(notificationId).orElseThrow(() -> new Exception("Not found notification"));
        if (noti.getRecipientId() == userId)
            noti.setIsRead("READ");
        return notificationRepository.save(noti);
    }
}
