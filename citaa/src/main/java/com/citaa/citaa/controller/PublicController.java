package com.citaa.citaa.controller;

import com.citaa.citaa.model.AnalyticsData;
import com.citaa.citaa.model.News;
import com.citaa.citaa.response.DataAnalyticsResponse;
import com.citaa.citaa.response.StatisticalResponse;
import com.citaa.citaa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/api")
public class PublicController {
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    ProjectService projectService;
    @Autowired
    CompetitionService competitionService;
    @Autowired
    AnalyticsDataService analyticsDataService;

    @GetMapping("/news/{newsId}")
    public ResponseEntity<News> getNewsById(@PathVariable("newsId")int id) throws Exception{
        return new ResponseEntity<>(newsService.getNewsById(id), HttpStatus.OK);
    }

    @GetMapping("/news")
    public ResponseEntity<Page<News>> filterNews(@RequestParam(defaultValue = "ALL") String field, @RequestParam(defaultValue = "0") int year,
                                                 @RequestParam int pageSize,
                                                 @RequestParam int pageNumber) throws Exception{
        return new ResponseEntity<>(newsService.filterNews(field,year,pageSize,pageNumber), HttpStatus.OK);
    }

    @GetMapping("/news/similar")
    public ResponseEntity<List<News>> getRelatedNews(@RequestParam(defaultValue = "ALL") String type,
                                                 @RequestParam int id) {
        return new ResponseEntity<>(newsService.getLatestNewsSimilarTo(id,type), HttpStatus.OK);
    }

    @GetMapping("/statistical")
    public ResponseEntity<StatisticalResponse> getStatistical(@RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "0") int month){
         return new ResponseEntity<>(userService.getStatisticalResponse(year,month),HttpStatus.OK);
    }

    @GetMapping("/analytics")
    public ResponseEntity<DataAnalyticsResponse> getAnalyticsData(@RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "0") int month){
        return new ResponseEntity<>(analyticsDataService.filterByYearAndMonth(year,month),HttpStatus.OK);
    }

    @GetMapping("/news/search")
    public ResponseEntity<Page<News>> searchNews(@RequestParam(defaultValue = "Not found") String query,
                                                 @RequestParam(defaultValue = "1")int pageSize,
                                                 @RequestParam(defaultValue = "0")int pageNumber) throws Exception{
        return new ResponseEntity<>(newsService.searchNews(query,pageSize,pageNumber),HttpStatus.OK);
    }
}
