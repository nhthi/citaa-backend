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

    private String message;

    private int recipientId;  // Id của người nhận thông báo

    private boolean isRead;  // Trạng thái đã đọc hay chưa đọc

    private LocalDateTime timestamp;  // Thời gian gửi thông báo

    private String type;  // Loại thông báo (nếu cần)

}
