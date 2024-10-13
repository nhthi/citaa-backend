package com.citaa.citaa.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvaluationManagementResponse {
    String fullName;
    int year;
    String projectName;
    List<String> fields;
    List<String> founderNames;
    String startupName;
}
