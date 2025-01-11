package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ConnectionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String fullName;
    String email;
    String phone;
    String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    String introduction;
    @Lob
    @Column(columnDefinition = "TEXT")
    String reason;
    @Lob
    @Column(columnDefinition = "TEXT")
    String questions;
    String preferredResponse;
    String responseTime;
    String linkAttachment;
}
