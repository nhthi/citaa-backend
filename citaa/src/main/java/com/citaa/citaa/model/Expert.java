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
    @ManyToMany
    @JsonIgnore
    List<Project> projects = new ArrayList<>();
}

