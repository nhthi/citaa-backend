package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Lob
    @Column(columnDefinition = "TEXT")
    String introduce;
    @Lob
    @Column(columnDefinition = "TEXT")
    String startUpIdea;
    @Lob
    @Column(columnDefinition = "TEXT")
    String formationProject;
    String email;
    String phone;
    double avg = 0;
    @ManyToOne
    Startup startup;
    @ManyToOne
    Investor investor;
    @OneToMany
    List<Evaluation> evaluation;
    @ElementCollection
    @Column(length = 1024)
    List<String> files;
    @OneToMany
    List<Comment> comments;
    @OneToMany
    List<User> reacts = new ArrayList<>();
    @ManyToMany
    List<Founder> founders;
}
