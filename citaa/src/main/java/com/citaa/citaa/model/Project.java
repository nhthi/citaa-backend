package com.citaa.citaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    boolean potential;
    double realTotalCapital;
    String currency;

    String field;
    String status = "UNVALID";
    LocalDateTime validAt;
    LocalDateTime updateAt;
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
    @OneToMany(mappedBy = "project")
    List<Investment> investments = new ArrayList<>();
    @OneToMany
    List<Evaluation> evaluation;
    @ElementCollection
    @Column(length = 1024)
    List<String> files;
    @OneToMany
    List<Comment> comments;
    @ManyToMany
    List<User> reactList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<React> reacts = new ArrayList<>();
    @ManyToMany
    List<Founder> founders;
    String address;
    String linkWeb;
    String businessModel;
    String mainTechnology;
    String pitchDeck;
    String businessRegistrationCertificate;
    // bang sáng chế
    String patent;
    String greenSustainableElement;

    int countExpert=0;

    @ManyToMany(mappedBy = "projects")
    List<Expert> experts = new ArrayList<>();
}
