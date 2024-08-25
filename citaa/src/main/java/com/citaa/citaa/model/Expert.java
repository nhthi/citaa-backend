package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Expert extends User {
    String field;
    String college;
    String education;
    List<String> certifications;
    int experienceYears;
}

