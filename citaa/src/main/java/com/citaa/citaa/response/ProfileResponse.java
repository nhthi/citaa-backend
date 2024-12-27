package com.citaa.citaa.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {

    //expert
    long countProjectExpert=0;
    double averagePoints=0;
    long countDoJudgeExpert = 0;

    //startup
    long countProjectStartup=0;
    long countCompetitionStartup=0;
    double totalCapitalStartup=0;
}
