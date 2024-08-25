package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String username;
    String password;
    String role;
    LocalDateTime createAt;
    String status;
}
