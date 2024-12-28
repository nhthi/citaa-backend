package com.citaa.citaa.controller;

import com.citaa.citaa.model.News;
import com.citaa.citaa.response.StatisticalResponse;
import com.citaa.citaa.service.CompetitionService;
import com.citaa.citaa.service.NewsService;
import com.citaa.citaa.service.ProjectService;
import com.citaa.citaa.service.UserService;
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
}
