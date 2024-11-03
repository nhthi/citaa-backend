package com.citaa.citaa.controller;

import com.citaa.citaa.model.Competition;
import com.citaa.citaa.request.ApplyCompetitionRequest;
import com.citaa.citaa.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/api/competition")
@RestController
public class CompetitionController {
    @Autowired
    CompetitionService competitionService;



    @PostMapping()
    public ResponseEntity<Competition> createCompetition(@RequestBody Competition competition, @RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.createCompetition(jwt,competition), HttpStatus.CREATED);
    }

    @PutMapping("/apply")
    public ResponseEntity<Competition> applyCompetitionResponseEntity(@RequestBody ApplyCompetitionRequest req, @RequestHeader("Authorization")String jwt) throws Exception {
        return new ResponseEntity<>(competitionService.applyCompetition(jwt,req.getIdCompetition(),req.getIdProject()),HttpStatus.OK);
    }
}
