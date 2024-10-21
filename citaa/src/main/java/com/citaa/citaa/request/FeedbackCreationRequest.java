package com.citaa.citaa.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackCreationRequest {
    String field;
    String title;
    String name;
    String email;
    String address;
    String content;
    int userId;
}
