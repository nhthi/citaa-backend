package com.citaa.citaa.request;


import com.citaa.citaa.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompetitionRequest {
    String name;
    String introduce;
    String content;
    List<String> fields = new ArrayList<>();
    List<String> files = new ArrayList<>();
    List<User> judges = new ArrayList<>();
    double first = 0;
    double second = 0;
    double third = 0;
    double mostVote = 0;
    LocalDate startAt;
    LocalDate endAt;
    int numberOfStages = 0;
    List<String> stages = new ArrayList<>();
    List<LocalDate> dateStages;
    List<String> descriptionStages;
}
