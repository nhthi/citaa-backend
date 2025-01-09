package com.citaa.citaa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private int recipientId;  // Id của người nhận thông báo

    private String isRead = "UNREAD";  // Trạng thái đã đọc hay chưa đọc

    private LocalDateTime readAt;  // Thời gian gửi thông báo

    private LocalDateTime timestamp;  // Thời gian gửi thông báo

    private String type;  // Loại thông báo (nếu cần)

    private String link;
}
