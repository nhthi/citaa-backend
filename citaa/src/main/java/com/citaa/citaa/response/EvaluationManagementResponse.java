package com.citaa.citaa.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvaluationManagementResponse {
    String fullName;
    LocalDateTime createAt;
    String projectName;
    List<String> fields;
    List<String> founderNames;
    String startupName;
    int projectId;
}
