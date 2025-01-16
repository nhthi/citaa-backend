package com.citaa.citaa.controller;

import com.citaa.citaa.model.Competition;
import com.citaa.citaa.model.EvaluationCompetition;
import com.citaa.citaa.model.Vote;
import com.citaa.citaa.request.ApplyCompetitionRequest;
import com.citaa.citaa.request.CreateRankingRequest;
import com.citaa.citaa.request.EvaluationCompetitionRequest;
import com.citaa.citaa.response.ApiResponse;
import com.citaa.citaa.service.CompetitionService;

import com.citaa.citaa.service.EvaluationCompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class CompetitionController {
    @Autowired
    CompetitionService competitionService;
    @Autowired
    EvaluationCompetitionService evaluationCompetitionService;

    @GetMapping("/api/competition/{competition-id}")
    public ResponseEntity<Competition> applyCompetitionResponseEntity(@PathVariable("competition-id") int id) throws Exception {
        return new ResponseEntity<>(competitionService.findCompetitionById(id),HttpStatus.OK);
    }

    @PutMapping("/api/competition/apply")
    public ResponseEntity<ApiResponse> applyCompetitionResponseEntity(@RequestBody ApplyCompetitionRequest req, @RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.applyCompetition(jwt,req.getIdCompetition(),req.getIdProject()),HttpStatus.OK);
    }

    @GetMapping("/api/competition/{competition-id}/vote")
    public ResponseEntity<List<Vote>> getVoteByCompetitionId(@PathVariable("competition-id") int id) throws Exception {
        return new ResponseEntity<>(competitionService.getVotesByCompetitionId(id),HttpStatus.OK);
    }

    @PutMapping("/api/competition/{competition-id}/vote/{project-id}")
    public ResponseEntity<ApiResponse> voteForProject(@RequestHeader("Authorization")String jwt,@PathVariable("project-id") int projectId,
    @PathVariable("competition-id") int competitionId) throws Exception {
        return new ResponseEntity<>(competitionService.voteForProject(jwt,competitionId,projectId),HttpStatus.OK);
    }

    @GetMapping("api/competitions")
    public ResponseEntity<Page<Competition>> getCompetitions(@RequestParam(required = false,defaultValue = "0") Integer pageNumber,
                                                             @RequestParam(required = false,defaultValue = "5") Integer pageSize,
                                                             @RequestParam(required = false) String year,
                                                             @RequestParam(required = false) String field,
                                                             @RequestParam(required = false) String status
    ) throws Exception {
        Page<Competition> res = competitionService.filterCompetition(pageNumber,pageSize,year,field,status);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/public/api/competition/count")
    public long countCompetition(){
        return competitionService.countCompetition();
    }

    @GetMapping("/api/competition/judge")
    public ResponseEntity<List<Competition>> getCompetitionByJudge(@RequestHeader("Authorization") String jwt,@RequestParam String status) throws Exception {
        return new ResponseEntity<>(competitionService.getCompetitionByJudge(jwt,status),HttpStatus.OK);
    }

    @GetMapping("/api/competition/startup")
    public ResponseEntity<Page<Competition>> getCompetitionByStartup(@RequestHeader("Authorization") String jwt,
                                                                     @RequestParam(defaultValue = "1") int pageSize,
                                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                                     @RequestParam(defaultValue = "0") int year,
                                                                     @RequestParam(defaultValue = "0") String field,
                                                                     @RequestParam(defaultValue = "0") String status) throws Exception {
        return new ResponseEntity<>(competitionService.findCompetitionByStartup(jwt,pageSize,pageNumber,year,field,status),HttpStatus.OK);
    }

    @PostMapping("/api/competition/scoring")
    public ResponseEntity<EvaluationCompetition> createEvaluationCompetition(@RequestBody EvaluationCompetitionRequest request, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(evaluationCompetitionService.createEvaluationCompetition(request,jwt),HttpStatus.CREATED);
    }

    @GetMapping("/api/competition/evaluation/{id}")
    public ResponseEntity<List<EvaluationCompetition>> getEvaluationCompetitionsByCompetitionAndJudge(@RequestHeader("Authorization") String jwt,@PathVariable int id) throws Exception {
        return new ResponseEntity<>(evaluationCompetitionService.findByJudgeAndCompetitionId(id,jwt),HttpStatus.OK);
    }

    @PostMapping("/api/competition/ranking/{id}")
    public ResponseEntity<Competition> createRanking(@PathVariable int id,@RequestBody List<CreateRankingRequest> requests, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.createRanking(id , requests),HttpStatus.CREATED);
    }
}
