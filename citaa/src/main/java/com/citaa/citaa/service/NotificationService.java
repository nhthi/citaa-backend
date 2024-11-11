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
                .isRead(false)
                .message(notification.getMessage())
                .timestamp(LocalDateTime.now())
                .recipientId(notification.getRecipientId())
                .type(notification.getType())
                .build());
    }

    public List<Notification> getNotificationsByUserId(int userId,boolean isRead) {
        return notificationRepository.findByUserId(userId,isRead);
    }

    public Notification readNotification(long notificationId,int userId) throws Exception {
        Notification noti = notificationRepository.findById(notificationId).orElseThrow(() -> new Exception("Not found notification"));
        if(noti.getRecipientId() == userId)
            noti.setRead(true);
        return notificationRepository.save(noti);
    }
}
