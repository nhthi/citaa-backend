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
    String topic;
    String heading;
    String fullName;
    String email;
    String address;
    String content;
}
