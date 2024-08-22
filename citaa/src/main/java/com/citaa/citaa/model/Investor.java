package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Investor extends User {
    String field;
    String riskTolerance;
    String companyName;
    int experienceYears;
    double investmentAmount;



}
