package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.ExpertRepository;
import com.citaa.citaa.repository.InvestorRepository;
import com.citaa.citaa.repository.StartupRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.ReplyFeedbackRequest;
import com.citaa.citaa.response.AdminCompetitionResponse;
import com.citaa.citaa.response.ApiResponse;
import com.citaa.citaa.service.*;
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
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private EvaluationService evaluationService;


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

    @GetMapping("/news")
    public ResponseEntity<Page<News>> filterNews(@RequestParam List<String> fields, @RequestParam(defaultValue = "0") int year,
                                                 @RequestParam int pageSize,
                                                 @RequestParam int pageNumber) throws Exception{
        return new ResponseEntity<>(newsService.filterNewsAdmin(fields,year,pageSize,pageNumber), HttpStatus.OK);
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<ApiResponse> deleteNews(@PathVariable int id, @RequestHeader("Authorization") String jwt) throws Exception{
        return new ResponseEntity<>(newsService.deleteNewsById(id),HttpStatus.OK);
    }

    @DeleteMapping("/competition/{id}")
    public ResponseEntity<ApiResponse> deleteCompetition(@PathVariable int id, @RequestHeader("Authorization") String jwt) throws Exception{
        return new ResponseEntity<>(competitionService.deleteCompetitionById(id),HttpStatus.OK);
    }

    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<ApiResponse> deleteFeedback(@PathVariable int id, @RequestHeader("Authorization") String jwt) throws Exception{
        return new ResponseEntity<>(feedbackService.deleteFeedbackById(id),HttpStatus.OK);
    }

    @PutMapping("/news/{id}")
    public ResponseEntity<News> updateNews(@PathVariable int id, @RequestHeader("Authorization") String jwt,@RequestBody News req) throws Exception{
        return new ResponseEntity<>(newsService.updateNewsById(id,req),HttpStatus.OK);
    }

    @PostMapping("/competition")
    public ResponseEntity<Competition> createCompetition(@RequestBody Competition competition, @RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.createCompetition(jwt,competition), HttpStatus.CREATED);
    }

    @GetMapping("/competition")
    public ResponseEntity<Page<Competition>> filterCompetition(@RequestParam List<String> fields,
                                                 @RequestParam(defaultValue = "0") int year,
                                                 @RequestParam int pageSize,
                                                 @RequestParam int pageNumber) throws Exception{
        return new ResponseEntity<>(competitionService.filterAllCompetition(pageNumber,pageSize,year,fields), HttpStatus.OK);
    }

    @GetMapping("/competition/statistical")
    public ResponseEntity<AdminCompetitionResponse> adminCompetitionStatistical(@RequestHeader("Authorization") String jwt) throws Exception{
        return new ResponseEntity<>(competitionService.getAdminCompetitionAnalysis(), HttpStatus.OK);
    }

    @PutMapping("/feedback/{feedback_id}")
    public ResponseEntity<Feedback> replyFeedback(@RequestHeader("Authorization") String jwt,
                                  @PathVariable int feedback_id,
                                  @RequestBody ReplyFeedbackRequest req) throws Exception {
        int user_id = userService.findByJwt(jwt).getId();

        return new ResponseEntity<>(feedbackService.replyFeedback(user_id,feedback_id,req.getReplyContent()),HttpStatus.OK);
    }

    @GetMapping("/feedback")
    public ResponseEntity<Page<Feedback>> getAllFeedbacks(@RequestHeader("Authorization") String jwt,
                                                                @RequestParam(defaultValue = "0") String status,
                                                                @RequestParam(defaultValue = "0") int year,
                                                                @RequestParam int pageSize,
                                                                @RequestParam int pageNumber
    ) throws Exception {
        return new ResponseEntity<>(feedbackService.filterFeedbackAdmin(year,status,pageNumber,pageSize),HttpStatus.OK);
    }

    @GetMapping("/startup/top-3")
    public ResponseEntity<List<Object[]>> getTop3Startups(@RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(projectService.getTop3StartupsWithMostProjects(),HttpStatus.OK);
    }

    @GetMapping("/expert/top-3")
    public ResponseEntity<List<Object[]>> getTop3Expert(@RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(evaluationService.getTop3ExpertMostEvaluation(),HttpStatus.OK);
    }
}
