package com.citaa.citaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@SuperBuilder
public class Expert extends User {
    String college;
    String education;
    @ElementCollection
    List<String> certifications;
    int experienceYears;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "expert_project",
            joinColumns = @JoinColumn(name = "expert_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnore
    List<Project> projects = new ArrayList<>();
}

