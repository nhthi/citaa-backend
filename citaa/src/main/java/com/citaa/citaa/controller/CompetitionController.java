package com.citaa.citaa.controller;

import com.citaa.citaa.model.Competition;
import com.citaa.citaa.model.Vote;
import com.citaa.citaa.request.ApplyCompetitionRequest;
import com.citaa.citaa.response.ApiResponse;
import com.citaa.citaa.service.CompetitionService;

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


    @PostMapping("/admin/api/competition")
    public ResponseEntity<Competition> createCompetition(@RequestBody Competition competition, @RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.createCompetition(jwt,competition), HttpStatus.CREATED);
    }
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
}
