package com.citaa.citaa.service;

import com.citaa.citaa.model.AnalyticsData;
import com.citaa.citaa.repository.AnalyticsDataRepository;
import com.google.analytics.data.v1beta.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.gax.core.FixedCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class QuickstartSample {

    @Autowired
    private AnalyticsDataRepository analyticsDataRepository;
//    @Scheduled(cron = "0 7 12 30 * ?")
    @Scheduled(cron = "0 0 1 1 * ?") // Lịch chạy hàng tháng vào lúc 01:00 AM vào ngày đầu tiên của tháng
    public void runScheduledTask() throws Exception {
        String propertyId = "470203401";
        sampleRunReport(propertyId);
    }

    private void sampleRunReport(String propertyId) throws Exception {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsPath == null || credentialsPath.isEmpty()) {
            System.out.println("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set.");
            return;
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create(settings)) {

            LocalDate currentDate = LocalDate.now();
            YearMonth previousMonth = YearMonth.from(currentDate).minusMonths(1);
            LocalDate firstDayOfPreviousMonth = previousMonth.atDay(1);
            LocalDate lastDayOfPreviousMonth = previousMonth.atEndOfMonth();

            RunReportRequest request = RunReportRequest.newBuilder()
                    .setProperty("properties/" + propertyId)
                    .addDimensions(Dimension.newBuilder().setName("date"))
                    .addMetrics(Metric.newBuilder().setName("sessions"))
                    .addDateRanges(DateRange.newBuilder()
                            .setStartDate(firstDayOfPreviousMonth.toString())
                            .setEndDate(lastDayOfPreviousMonth.toString())
                            .build())
                    .build();
            RunReportResponse response = analyticsData.runReport(request);

            long totalSessions = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            for (Row row : response.getRowsList()) {
                String sessionsStr = row.getMetricValues(0).getValue();
                long sessions = Long.parseLong(sessionsStr);
                totalSessions += sessions;
            }

            // Lưu tổng số lượt truy cập vào cơ sở dữ liệu
            AnalyticsData analyticsDataMonth = new AnalyticsData();
            analyticsDataMonth.setDate(firstDayOfPreviousMonth);
            analyticsDataMonth.setTotalSessions(totalSessions);
            analyticsDataRepository.save(analyticsDataMonth);
        }
    }
}
