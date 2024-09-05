package com.citaa.citaa.service;

import com.citaa.citaa.model.Evaluation;
import com.citaa.citaa.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {
    @Autowired
    EvaluationRepository evaluationRepository;

    public Evaluation createEvaluation(Evaluation evalution){
        return evaluationRepository.save(evalution);
    }
}
