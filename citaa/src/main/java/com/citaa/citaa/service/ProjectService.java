package com.citaa.citaa.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.citaa.citaa.model.Evaluation;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.repository.EvaluationRepository;
import com.citaa.citaa.repository.ProjectRepository;
import com.citaa.citaa.request.ProjectCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    StartupService startupService;
    @Autowired
    EvaluationRepository evaluationRepository;

    public double calculateAveragePoints(int projectId){
        double avg = 0;
        List<Evaluation> evaluations = evaluationRepository.findByProjectId(projectId);

        double points = 0;
        for(Evaluation item: evaluations){
            points += item.getPoints();
        }
        if(!evaluations.isEmpty())
            avg =  points/evaluations.size();

        return avg;
    }

    public Project createProject(ProjectCreationRequest request, Startup startup){
        return projectRepository.save(Project.builder()
                        .name(request.getName())
                        .field(request.getField())
                        .files(request.getFiles())
                        .realTotalCapital(request.getRealTotalCapital())
                        .createAt(LocalDateTime.now())
                        .startup(startup)
                        .valid(false)
                        .currency(request.getCurrency())
                        .formationProject(request.getFormationProject())
                        .introduce(request.getIntroduce())
                        .startUpIdea(request.getStartUpIdea())
                        .founders(request.getFounders())
                        .phone(request.getPhone())
                        .email(request.getEmail())
                .build());
    }

    public void setValid(int projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new RuntimeException("Project not found!"));


        double avg = calculateAveragePoints(projectId);

        project.setAvg(avg);

        if(avg>=50)
            project.setValid(true);
        else
            project.setValid(false);

        projectRepository.save(project);

    }


    public List<Project> getProjectsByJwt(String jwt) throws Exception {
        Startup startup = startupService.getStartupByJwt(jwt);
        return projectRepository.findByStartupId(startup.getId());
    }

    public List<Project> getProjectsByStartupId(int id) throws Exception {
        return projectRepository.findByStartupId(id);
    }

    public Project getProjectById(int id) throws Exception {
        return projectRepository.findById(id)
                .orElseThrow(()-> new Exception(("Project not found!")));
    }




}


