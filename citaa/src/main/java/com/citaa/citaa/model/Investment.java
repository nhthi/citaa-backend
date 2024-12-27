package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    Project project;

    @ManyToOne
    Investor investor;

    double amount;

    LocalDateTime investmentDate;

    String investmentDetails;

    String feedBack;

    String status;

    LocalDateTime creatAt;
}
