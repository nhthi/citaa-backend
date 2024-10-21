package com.citaa.citaa.controller;

import com.citaa.citaa.model.Feedback;
import com.citaa.citaa.request.FeedbackCreationRequest;
import com.citaa.citaa.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;
    @PostMapping
    ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackCreationRequest request) throws Exception {
        return new ResponseEntity<>(feedbackService.createFeedback(request), HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<Feedback>> getListFeedback(){
        return new ResponseEntity<>(feedbackService.getListFeedback(), HttpStatus.OK);
    }
}
