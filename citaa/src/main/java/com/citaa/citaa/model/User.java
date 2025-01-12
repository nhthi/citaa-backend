package com.citaa.citaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String fullName;
    String address;
    LocalDate dob;
    String valid = "UNVALID";
    LocalDateTime validAt;
    LocalDateTime requestAt;
    String email;
    @OneToOne(cascade = CascadeType.ALL)
    Account account;
    boolean gender;
    String avatar;
    String coverPhoto;
    String bio;
    @ElementCollection
    List<String> fields = new ArrayList<>();
    String phone;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<React> reacts;
}
