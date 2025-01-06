package com.citaa.citaa.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationCompetitionRequest {
    int points;
    String content;
    String comment;
    int projectId;
    int competitionId;
}
