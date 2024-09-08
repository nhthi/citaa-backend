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
    String introduce;
    @Lob
    String startUpIdea;
    @Lob
    String formationProject;
    String email;
    String phone;
    @ManyToOne
    Startup startup;
    @ManyToOne
    Investor investor;
    @OneToMany
    List<Evaluation> evaluation;
    @ElementCollection
    List<String> files;
    @OneToMany
    List<Comment> comments;
    @OneToMany
    List<React> reacts;
    @ManyToMany
    List<Founder> founders;
}
