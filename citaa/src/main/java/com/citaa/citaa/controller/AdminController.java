package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.ExpertRepository;
import com.citaa.citaa.repository.InvestorRepository;
import com.citaa.citaa.repository.StartupRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.service.CustomerUserDetailService;
import com.citaa.citaa.service.NewsService;
import com.citaa.citaa.service.ProjectService;
import com.citaa.citaa.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/api")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    ProjectService projectService;
    @PostMapping("/add-to-expert/{expertId}/{projectId}")
    public ResponseEntity<Expert> addProjectToExpert( @PathVariable int expertId, @PathVariable int projectId) throws Exception {
        return new ResponseEntity<>(userService.addProjectToExpert(projectId,expertId), HttpStatus.CREATED);
    }
    @GetMapping("/all-startup")
    public ResponseEntity<Page<Startup>> getAllStartup(@RequestParam("pageSize") int pageSize,
                                                       @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllStartups(pageSize,pageNumber),HttpStatus.OK);
    }

    @GetMapping("/all-expert")
    public ResponseEntity<Page<Expert>> getAllExpert(@RequestParam("pageSize") int pageSize,
                                                       @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllExpert(pageSize,pageNumber),HttpStatus.OK);
    }

    @GetMapping("/all-investor")
    public ResponseEntity<Page<Investor>> getAllInvestor(@RequestParam("pageSize") int pageSize,
                                                        @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllInvestor(pageSize,pageNumber),HttpStatus.OK);
    }

    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody News request,@RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(newsService.createNews(request,jwt), HttpStatus.CREATED);
    }

    @PutMapping("/project/verify/{projectId}")
    public ResponseEntity<Project> verifyProject(@PathVariable("projectId")int id, @RequestHeader("Authorization") String jwt) throws Exception{
        return new ResponseEntity<>(projectService.verifyProject(id,jwt), HttpStatus.OK);
    }
}
