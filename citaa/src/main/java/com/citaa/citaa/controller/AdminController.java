package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.Expert;
import com.citaa.citaa.model.Investor;
import com.citaa.citaa.model.News;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.repository.ExpertRepository;
import com.citaa.citaa.repository.InvestorRepository;
import com.citaa.citaa.repository.StartupRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.service.CustomerUserDetailService;
import com.citaa.citaa.service.NewsService;
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
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CustomerUserDetailService customerUserDetailService;
    @Autowired
    StartupRepository startupRepository;
    @Autowired
    ExpertRepository expertRepository;
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;

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


    @GetMapping("/news")
    public ResponseEntity<List<News>> getListNews(){
        return new ResponseEntity<>(newsService.getListNews(), HttpStatus.OK);
    }

    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody News request,@RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(newsService.createNews(request,jwt), HttpStatus.CREATED);
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<News> getNewsById(@PathVariable("newsId")int id) throws Exception{
        return new ResponseEntity<>(newsService.getNewsById(id), HttpStatus.OK);
    }
}
