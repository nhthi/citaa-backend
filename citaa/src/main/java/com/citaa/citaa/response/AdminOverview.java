package com.citaa.citaa.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminOverview {
    long newAccount = 0;
    long countFeedback = 0;
    long countReplyFeedback = 0;
    long countComment = 0;
}
