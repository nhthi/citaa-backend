package com.citaa.citaa.service;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.*;
import java.util.Collections;
import java.util.List;

public class GoogleAnalyticsFetcher {

    public void fetchData() throws Exception {
        AnalyticsReporting analytics = AnalyticsService.getAnalyticsService();

        // Tạo một yêu cầu báo cáo
        GetReportsRequest request = new GetReportsRequest()
                .setReportRequests(Collections.singletonList(
                        new ReportRequest()
                                .setViewId("338921118")  // ID của View trong Google Analytics
                                .setDateRanges(Collections.singletonList(
                                        new DateRange().setStartDate("7daysAgo").setEndDate("today")))
                                .setMetrics(Collections.singletonList(new Metric().setExpression("ga:sessions")))
                ));

        // Thực thi yêu cầu
        GetReportsResponse response = analytics.reports().batchGet(request).execute();

        // Xử lý dữ liệu trả về
        List<Report> reports = response.getReports();
        for (Report report : reports) {
            for (MetricHeaderEntry header : report.getColumnHeader().getMetricHeader().getMetricHeaderEntries()) {
                System.out.println("Metric: " + header.getName());
            }
        }
    }
}

