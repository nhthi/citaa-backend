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
    public ResponseEntity<Page<News>> filterNews(@RequestParam(defaultValue = "ALL") String field, @RequestParam(defaultValue = "2024") String year,
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
    public ResponseEntity<StatisticalResponse> getStatistical(){
        long countExpert = userService.countExpert();
        long countStartup = userService.countStartup();
        long countInvestor = userService.countInvestor();
        long countProject = projectService.countProject();
        long countCompetition = competitionService.countCompetition();
        StatisticalResponse response = new StatisticalResponse();
        response.setCountExpert(countExpert);
        response.setCountStartup(countStartup);
        response.setCountInvestor(countInvestor);
        response.setCountProject(countProject);
        response.setCountCompetition(countCompetition);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
