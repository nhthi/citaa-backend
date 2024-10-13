package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String field;
    String title;
    String name;
    String email;
    String address;
    String content;
    @ManyToOne
    User user;
}
