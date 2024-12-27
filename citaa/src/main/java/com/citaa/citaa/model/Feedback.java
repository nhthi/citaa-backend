package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String topic;
    String heading;
    String fullName;
    String email;
    String address;
    @Lob
    @Column(columnDefinition = "TEXT")
    String content;
    LocalDateTime createdAt;

    String replyContent;
    @OneToOne(cascade = CascadeType.ALL)
    User adminReply = null;
    String status;
    LocalDateTime replyAt;
}
