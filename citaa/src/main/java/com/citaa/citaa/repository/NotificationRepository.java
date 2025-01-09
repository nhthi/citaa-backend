package com.citaa.citaa.repository;

import com.citaa.citaa.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Lấy tất cả thông báo
    List<Notification> findByRecipientId(int userId);

    // Lấy thông báo chưa đọc của một người dùng
    @Query("select n from Notification n where n.recipientId = :userId and (:status= '0' or n.isRead = :status)")
    List<Notification> findByRecipientIdAndIsRead(int userId, String status );

    // Lấy thông báo theo loại cho một người dùng
    List<Notification> findByRecipientIdAndType(int userId, String type);

    // Lấy thông báo chưa đọc theo loại cho một người dùng
    @Query("select n from Notification n where n.recipientId = :userId and (:status= '0' or n.isRead = :status) and (:type= '0' or n.type = :type)")
    List<Notification> findByRecipientIdAndTypeAndIsRead(int userId, String type, String status);

    // Lấy tất cả thông báo theo loại
    List<Notification> findByType(String type);

}
