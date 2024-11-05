package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "user_id")
    int userId; // Chỉ lưu ID của người bình chọn

    @Column(name = "project_id")
    int projectId; // Chỉ lưu ID của dự án

    @Column(name = "competition_id")
    int competitionId; // Chỉ lưu ID của cuộc thi

    LocalDateTime createdAt; // Thời gian bình chọn

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
