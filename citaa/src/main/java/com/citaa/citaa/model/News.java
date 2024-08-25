package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String type;
    String content;
    @ElementCollection
    List<String> urls;
    @ManyToOne
    User admin;
}
