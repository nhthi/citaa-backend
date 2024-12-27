package com.citaa.citaa.controller;

import com.citaa.citaa.model.Investment;
import com.citaa.citaa.request.FeedbackInvestmentRequest;
import com.citaa.citaa.request.InvestmentRequest;
import com.citaa.citaa.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @PostMapping
    public Investment createInvestment(@RequestBody InvestmentRequest investmentRequestDTO) {
        return investmentService.createInvestment(
                investmentRequestDTO.getProjectId(),
                investmentRequestDTO.getInvestorId(),
                investmentRequestDTO.getAmount(),
                investmentRequestDTO.getDetails()
        );
    }

    @PutMapping("/feedback/{id}")
    public Investment feedbackInvestment(@RequestHeader("Authorization") String jwt,@PathVariable int id, FeedbackInvestmentRequest req) throws Exception {
        return investmentService.feedback(id,req.getContent());
    }


}
