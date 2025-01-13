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
public class ConnectionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "investor_id", nullable = false)
    private User investor;

    @OneToOne(cascade = CascadeType.ALL) // Thêm cascade để tự động lưu ConnectionInfo
    @JoinColumn(name = "connection_info_id")
    ConnectionInfo connectionInfo;
    @Lob
    @Column(columnDefinition = "TEXT")
    String response;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    private String status; // PENDING, ACCEPTED, REJECTED
    private LocalDateTime requestDate;
    private LocalDateTime responseDate;
}
