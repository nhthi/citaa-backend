package com.citaa.citaa.request;

import com.citaa.citaa.model.Competition;
import com.citaa.citaa.model.Project;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRankingRequest {

    int projectId;

    int rank = 100; // Vị trí xếp hạng: 1, 2, 3, v.v.

    double score = 0; // Điểm của startup

    int vote = 0;

    boolean isVotest = false;
}
