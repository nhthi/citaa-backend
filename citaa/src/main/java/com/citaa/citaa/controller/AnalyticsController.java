package com.citaa.citaa.controller;

import com.citaa.citaa.service.AnalyticsDataService;
import com.citaa.citaa.service.GoogleAnalyticsFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsDataService analyticsDataService;

    @GetMapping("/fetch")
    public ResponseEntity<String> fetchData() {
        try {
            GoogleAnalyticsFetcher fetcher = new GoogleAnalyticsFetcher();
            fetcher.fetchData();
            return ResponseEntity.ok("Data fetched successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching data");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
