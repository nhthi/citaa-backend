package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Evalution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int points;
    String content;
    Date createAt;
   @ManyToOne
   User expert;
}
