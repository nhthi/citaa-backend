package com.citaa.citaa.controller;

import com.citaa.citaa.model.Evaluation;
import com.citaa.citaa.service.EvaluationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequestMapping("/api/expert/evaluation")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvaluationController {
    @Autowired
    EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody Evaluation evaluation){
        return new ResponseEntity<>(evaluationService.createEvaluation(evaluation), HttpStatus.CREATED);
    }

}
