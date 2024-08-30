package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.time.LocalDateTime;

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
    Date dob;
    boolean valid;
    String email;
    @OneToOne(cascade = CascadeType.ALL)
    Account account;
    boolean gender;
//    String major;
//    String course;
//    String college;




}
