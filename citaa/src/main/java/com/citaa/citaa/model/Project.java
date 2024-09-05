package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    LocalDateTime createAt;
    boolean valid;
    double realTotalCapital;
    String currency;
    String field;
    String description;
    String introduce;
    @ManyToOne
    Startup startup;
    @ManyToOne
    Investor investor;
    @OneToOne
    Evalution evalution;
    @ElementCollection
    List<String> files;
    @OneToMany
    List<Comment> comments;
    @OneToMany
    List<React> reacts;
}
