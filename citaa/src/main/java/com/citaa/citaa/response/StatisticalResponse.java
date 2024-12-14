package com.citaa.citaa.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticalResponse {
    private long countCompetition=0;
    private long countStartup=0;
    private long countExpert=0;
    private long countInvestor=0;
    private long countProject=0;
}
