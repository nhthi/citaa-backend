package com.citaa.citaa.service;

import com.citaa.citaa.model.Competition;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.CompetitionRepository;
import com.citaa.citaa.repository.TimelineEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CompetitionService {
    @Autowired
    UserService userService;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    TimelineEventRepository timelineEventRepository;
    @Autowired
    ProjectService projectService;

    public Competition createCompetition(String jwt,Competition req) throws Exception {
        User admin = userService.findByJwt(jwt);
        Competition newCompetition = competitionRepository.save(Competition.builder()
                        .name(req.getName())
                        .introduce(req.getIntroduce())
                        .content(req.getContent())
                        .files(req.getFiles())
                        .fields(req.getFields())
                        .rewards(req.getRewards())
                        .judges(req.getJudges())
                        .timelineEvents(req.getTimelineEvents())
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .startAt(req.getStartAt())
                        .endAt(req.getEndAt())
                        .admin(req.getAdmin())
                .build());
        return newCompetition;
    }


    public Competition findCompetitionById(int id) throws Exception {
        Optional<Competition> competition = competitionRepository.findById(id);
        if(competition.isEmpty()){
            throw new Exception("Competition not found with id : "+id);
        }
        return competition.get();
    }


    public Competition applyCompetition(String jwt,int idCompetition, int projectId) throws Exception {
        User candidate = userService.findByJwt(jwt);
        Project project = projectService.findProjectById(projectId);
        Competition competition = findCompetitionById(idCompetition);
        if (competition.getStartupAppliedTimes().containsKey(candidate)) {
            throw new Exception("This candidate has already applied to the competition.");
        }

        competition.getStartupAppliedTimes().put((Startup) candidate, LocalDateTime.now());
        competition.getProjects().add(project);
        return competitionRepository.save(competition);
    }
}
