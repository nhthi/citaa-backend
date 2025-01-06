package com.citaa.citaa.service;

import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.EvaluationCompetitionRepository;
import com.citaa.citaa.repository.EvaluationRepository;
import com.citaa.citaa.repository.ProjectRepository;
import com.citaa.citaa.request.EvaluationCompetitionRequest;
import com.citaa.citaa.request.EvaluationCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.JulianFields;
import java.util.List;

@Service
public class EvaluationCompetitionService {

    @Autowired
    private EvaluationCompetitionRepository evaluationCompetitionRepository;
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectRepository projectRepository;


    public EvaluationCompetition createEvaluationCompetition(EvaluationCompetitionRequest request, String jwt) throws Exception {
        Competition competition = competitionService.findCompetitionById(request.getCompetitionId());
        Project project = projectService.findProjectById(request.getProjectId());
        if (!competition.getProjects().contains(project)) {
            throw new Exception("Dự án này không có trong cuộc thi");
        }
        User judge = userService.findByJwt(jwt);
        if (!competition.getJudges().contains(judge)) {
            throw new Exception("Đây không phải là giám khảo của cuộc thi này");
        }

        boolean isEvaluated = evaluationCompetitionRepository.existsByCompetitionIdAndJudgeIdAndProjectId(request.getCompetitionId(), judge.getId(), request.getProjectId());
        if (isEvaluated) {
            throw new Exception("Giám khảo đã chấm điểm dự án này rồi");
        }


        return evaluationCompetitionRepository.save(EvaluationCompetition.builder()
                .content(request.getContent())
                .points(request.getPoints())
                .comment(request.getComment())
                .createAt(LocalDateTime.now())
                .judge(judge)
                .projectId(project.getId())
                .competitionId(competition.getId())
                .build());
    }


    public List<EvaluationCompetition> findByJudgeAndCompetitionId(int competitionId, String jwt) throws Exception {
        User user = userService.findByJwt(jwt);
        return evaluationCompetitionRepository.findByCompetitionIdAndJudgeId(competitionId, user.getId());
    }

    public List<EvaluationCompetition> findByJudgeIdAndCompetitionId(int competitionId, int judgeId) throws Exception {
        return evaluationCompetitionRepository.findByCompetitionIdAndJudgeId(competitionId, judgeId);
    }

    public List<EvaluationCompetition> findByCompetitionId(int competitionId) throws Exception {
        return evaluationCompetitionRepository.findByCompetitionId(competitionId);
    }
}
