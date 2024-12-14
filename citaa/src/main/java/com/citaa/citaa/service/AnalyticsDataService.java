package com.citaa.citaa.service;

import com.citaa.citaa.model.AnalyticsData;
import com.citaa.citaa.repository.AnalyticsDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsDataService {

    @Autowired
    private AnalyticsDataRepository repository;

    public void saveAnalyticsData(String metricName, Long value) {
        AnalyticsData data = new AnalyticsData();
        data.setMetricName(metricName);
        data.setValue(value);
        repository.save(data);
    }
}

