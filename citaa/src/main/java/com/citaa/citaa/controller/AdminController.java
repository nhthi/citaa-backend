package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.ExpertRepository;
import com.citaa.citaa.repository.InvestorRepository;
import com.citaa.citaa.repository.StartupRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.CompetitionRequest;
import com.citaa.citaa.request.ExpertProjectRequest;
import com.citaa.citaa.request.ReplyFeedbackRequest;
import com.citaa.citaa.request.UpdateStatusUserRequest;
import com.citaa.citaa.response.*;
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
    @Autowired
    private EvaluationCompetitionService evaluationCompetitionService;

    @PostMapping("/add-to-expert/{projectId}")
    public ResponseEntity<Project> addProjectToExpert(@PathVariable int projectId, @RequestBody ExpertProjectRequest req) throws Exception {
        return new ResponseEntity<>(userService.addProjectToExpert(projectId, req.getExpertIds()), HttpStatus.CREATED);
    }

    @GetMapping("/all-startup")
    public ResponseEntity<Page<Startup>> getAllStartup(@RequestParam("pageSize") int pageSize,
                                                       @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllStartups(pageSize, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/all-expert")
    public ResponseEntity<Page<Expert>> getAllExpert(@RequestParam("pageSize") int pageSize,
                                                     @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllExpert(pageSize, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/all-admin")
    public ResponseEntity<Page<User>> getAllAdmin(@RequestParam("pageSize") int pageSize,
                                                  @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllAdmin(pageSize, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/all-investor")
    public ResponseEntity<Page<Investor>> getAllInvestor(@RequestParam("pageSize") int pageSize,
                                                         @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllInvestor(pageSize, pageNumber), HttpStatus.OK);
    }

    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody News request, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(newsService.createNews(request, jwt), HttpStatus.CREATED);
    }

    @PutMapping("/project/verify/{projectId}")
    public ResponseEntity<Project> verifyProject(@PathVariable("projectId") int id, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(projectService.verifyProject(id, jwt), HttpStatus.OK);
    }

    @GetMapping("/news")
    public ResponseEntity<Page<News>> filterNews(@RequestParam List<String> fields, @RequestParam(defaultValue = "0") int year,
                                                 @RequestParam int pageSize,
                                                 @RequestParam int pageNumber,
                                                 @RequestParam int adminReply) throws Exception {
        return new ResponseEntity<>(newsService.filterNewsAdmin(fields, year, pageSize, pageNumber, adminReply), HttpStatus.OK);
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<ApiResponse> deleteNews(@PathVariable int id, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(newsService.deleteNewsById(id), HttpStatus.OK);
    }

    @DeleteMapping("/competition/{id}")
    public ResponseEntity<ApiResponse> deleteCompetition(@PathVariable int id, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.deleteCompetitionById(id), HttpStatus.OK);
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable int id, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(projectService.deleteProjectById(id), HttpStatus.OK);
    }

    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<ApiResponse> deleteFeedback(@PathVariable int id, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(feedbackService.deleteFeedbackById(id), HttpStatus.OK);
    }

    @PutMapping("/news/{id}")
    public ResponseEntity<News> updateNews(@PathVariable int id, @RequestHeader("Authorization") String jwt, @RequestBody News req) throws Exception {
        return new ResponseEntity<>(newsService.updateNewsById(id, req), HttpStatus.OK);
    }

    @PostMapping("/competition")
    public ResponseEntity<Competition> createCompetition(@RequestBody CompetitionRequest competition, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.createCompetition(jwt, competition), HttpStatus.CREATED);
    }

    @PutMapping("/competition/{id}")
    public ResponseEntity<Competition> updateCompetition(@RequestBody CompetitionRequest competition, @RequestHeader("Authorization") String jwt,@PathVariable int id) throws Exception {
        return new ResponseEntity<>(competitionService.updateCompetition(id, competition), HttpStatus.OK);
    }

    @GetMapping("/competition")
    public ResponseEntity<Page<Competition>> filterCompetition(@RequestParam List<String> fields,
                                                               @RequestParam(defaultValue = "0") int year,
                                                               @RequestParam int pageSize,
                                                               @RequestParam int pageNumber) throws Exception {
        return new ResponseEntity<>(competitionService.filterAllCompetition(pageNumber, pageSize, year, fields), HttpStatus.OK);
    }

    @GetMapping("/competition/statistical")
    public ResponseEntity<AdminCompetitionResponse> adminCompetitionStatistical(@RequestHeader("Authorization") String jwt,
                                                                                @RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "0") int month) throws Exception {
        return new ResponseEntity<>(competitionService.getAdminCompetitionAnalysis(year,month), HttpStatus.OK);
    }

    @PutMapping("/feedback/{feedback_id}")
    public ResponseEntity<Feedback> replyFeedback(@RequestHeader("Authorization") String jwt,
                                                  @PathVariable int feedback_id,
                                                  @RequestBody ReplyFeedbackRequest req) throws Exception {
        User admin = userService.findByJwt(jwt);
        return new ResponseEntity<>(feedbackService.replyFeedback(admin, feedback_id, req.getReplyContent()), HttpStatus.OK);
    }

    @GetMapping("/feedback")
    public ResponseEntity<Page<Feedback>> getAllFeedbacks(@RequestHeader("Authorization") String jwt,
                                                          @RequestParam(defaultValue = "0") String status,
                                                          @RequestParam(defaultValue = "0") int year,
                                                          @RequestParam int pageSize,
                                                          @RequestParam int pageNumber
    ) throws Exception {
        return new ResponseEntity<>(feedbackService.filterFeedbackAdmin(year, status, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/startup/top-3")
    public ResponseEntity<List<Object[]>> getTop3Startups(@RequestHeader("Authorization") String jwt, @RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "0") int month) throws Exception {
        return new ResponseEntity<>(projectService.getTop3StartupsWithMostProjects(year, month), HttpStatus.OK);
    }

    @GetMapping("/expert/top-3")
    public ResponseEntity<List<Object[]>> getTop3Expert(@RequestHeader("Authorization") String jwt, @RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "0") int month) throws Exception {
        return new ResponseEntity<>(evaluationService.getTop3ExpertMostEvaluation(year, month), HttpStatus.OK);
    }

    @PutMapping("/user/status")
    public ResponseEntity<User> updateStatusUser(@RequestHeader("Authorization") String jwt, @RequestBody UpdateStatusUserRequest req) throws Exception {
        System.out.println(req.getStatus());
        return new ResponseEntity<>(userService.updateStatus(req.getUserId(), req.getStatus()), HttpStatus.OK);
    }

    @GetMapping("/overview")
    public ResponseEntity<AdminOverview> getAdminOverview(@RequestHeader("Authorization") String jwt, @RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "0") int month) throws Exception {
        return new ResponseEntity<>(userService.getAdminOverview(jwt, year, month), HttpStatus.OK);
    }

    @GetMapping("/overview-project")
    public ResponseEntity<AdminProjectOverview> getAdminProjectOverview(@RequestHeader("Authorization") String jwt, @RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "0") int month) throws Exception {
        return new ResponseEntity<>(userService.getAdminProjectOverview(jwt, year, month), HttpStatus.OK);
    }

    @GetMapping("/competition/evaluation/judge/{id}")
    public ResponseEntity<List<EvaluationCompetition>> getEvaluationCompetitionsByCompetitionAndJudge(@RequestParam int judgeId,@PathVariable int id) throws Exception {
        return new ResponseEntity<>(evaluationCompetitionService.findByJudgeIdAndCompetitionId(id,judgeId),HttpStatus.OK);
    }

    @GetMapping("/competition/evaluation/{id}")
    public ResponseEntity<List<EvaluationCompetition>> getEvaluationCompetitionsByCompetition(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(evaluationCompetitionService.findByCompetitionId(id),HttpStatus.OK);
    }

    @PutMapping("/valid-user/{userId}")
    public ResponseEntity<MessageResponse> requestValidUser(@PathVariable int userId) throws Exception {
        return new ResponseEntity<>(userService.validUser(userId),HttpStatus.OK);
    }
}
