package com.citaa.citaa.request;


import com.citaa.citaa.model.Founder;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectCreationRequest {
    String name;
    double realTotalCapital;
    String field;
    List<String> files;
    String currency;
    String introduce;
    String startUpIdea;
    String formationProject;
    List<Founder> founders;
}
