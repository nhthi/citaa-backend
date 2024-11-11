package com.citaa.citaa.repository;

import com.citaa.citaa.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.recipientId=:userId and n.isRead=:isRead")
    List<Notification> findByUserId(int userId,boolean isRead);
}
