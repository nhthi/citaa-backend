package com.citaa.citaa.service;

import com.citaa.citaa.model.Evaluation;
import com.citaa.citaa.model.Expert;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.EvaluationRepository;
import com.citaa.citaa.repository.ProjectRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.EvaluationCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EvaluationService {
    @Autowired
    EvaluationRepository evaluationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectService projectService;

    public Evaluation createEvaluation(EvaluationCreationRequest request) throws Exception {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new Exception(("Project not found!")));

        User expert = userRepository.findById(request.getExpertId())
                .orElseThrow(() -> new Exception(("Expert not found!")));

        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                        .content(request.getContent())
                        .points(request.getPoints())
                        .createAt(LocalDateTime.now())
                        .expert(expert)
                        .projectId(project.getId())
                .build());

        project.getEvaluation().add(evaluation);
        projectRepository.save(project);
        projectService.setValid(project.getId());

        return  evaluation;
    }

    public List<Evaluation> getEvaluationByExpertId(int expertId){
        return evaluationRepository.findByExpertId(expertId);
    }
}
