package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Investor extends User {
    String field;
    String riskTolerance;
    String companyName;
    int experienceYears;
    double investmentAmount;


}
