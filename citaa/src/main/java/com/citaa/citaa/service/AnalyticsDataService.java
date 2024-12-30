package com.citaa.citaa.service;

import com.citaa.citaa.model.AnalyticsData;
import com.citaa.citaa.repository.AnalyticsDataRepository;
import com.citaa.citaa.response.DataAnalyticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsDataService {

    @Autowired
    private AnalyticsDataRepository repository;

    public DataAnalyticsResponse filterByYearAndMonth(int year, int month) {
        DataAnalyticsResponse response = new DataAnalyticsResponse();
        response.setMonthData(repository.findByYearAndMonth(year,month));
        response.setYearData(repository.findByYearAndMonth(year,0));
        return response;
    }

}

