package com.citaa.citaa.controller;

import com.citaa.citaa.model.Evaluation;
import com.citaa.citaa.request.EvaluationCreationRequest;
import com.citaa.citaa.service.EvaluationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api/expert/evaluation")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvaluationController {
    @Autowired
    EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody EvaluationCreationRequest request) throws Exception {
        return new ResponseEntity<>(evaluationService.createEvaluation(request), HttpStatus.CREATED);
    }

    @GetMapping("/{expertId}")
    public ResponseEntity<List<Evaluation>> getEvaluationByExpertId(@PathVariable int expertId) throws Exception {
        return new ResponseEntity<>(evaluationService.getEvaluationByExpertId(expertId), HttpStatus.OK);
    }

}
