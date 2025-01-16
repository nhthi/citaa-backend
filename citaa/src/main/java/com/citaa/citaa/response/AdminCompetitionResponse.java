package com.citaa.citaa.response;

import com.citaa.citaa.model.Competition;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCompetitionResponse {
    int onGoingCompetition = 0;
    int endedCompetition = 0;
    int upcomingCompetition = 0;

    int agricultureCompetition = 0;
    int aquacultureCompetition = 0;
    int otherCompetition = 0;

    List<Competition> topJoin = new ArrayList<>();
    List<Object[]> topReward = new ArrayList<>();
    List<Object[]> topVote = new ArrayList<>();

}
