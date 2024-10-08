package com.citaa.citaa.request;

import com.citaa.citaa.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    String fullName;
    String address;
    Date dob;
    String email;
    Account account;
    boolean gender;
    String avatar;
    String coverPhoto;
    String bio;
    // start up
    String studentId;
    String cohort;
    String college;
    // investor
    List<String> fields;
    String riskTolerance;
    String companyName;
    int experienceYears;
    double investmentAmount;
    //expert & investor
//    String field;
//    String college;
    // expert
    String education;
    List<String> certifications;
//    int experienceYears;
}
