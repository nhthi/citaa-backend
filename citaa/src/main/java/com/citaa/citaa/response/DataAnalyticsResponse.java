package com.citaa.citaa.response;

import com.citaa.citaa.model.AnalyticsData;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataAnalyticsResponse {
    List<AnalyticsData> monthData;
    List<AnalyticsData> yearData;
}
