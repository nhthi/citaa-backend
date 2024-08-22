package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
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
    Date createAt;
    boolean valid;
    double realTotalCapital;
    String field;
    String description;
    @ManyToOne
    User startup;
    @ManyToOne
    User investor;
    @OneToOne
    Evalution evalution;
    @ElementCollection
    List<String> files;
    @OneToMany
    List<Comment> comments;
    @OneToMany
    List<React> reacts;
}
