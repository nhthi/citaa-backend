package com.citaa.citaa.controller;

import com.citaa.citaa.model.News;
import com.citaa.citaa.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api")
public class PublicController {
    @Autowired
    NewsService newsService;

    @GetMapping("/news/{newsId}")
    public ResponseEntity<News> getNewsById(@PathVariable("newsId")int id) throws Exception{
        return new ResponseEntity<>(newsService.getNewsById(id), HttpStatus.OK);
    }
}
