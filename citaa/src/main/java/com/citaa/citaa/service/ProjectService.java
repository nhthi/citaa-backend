package com.citaa.citaa.service;

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
                .build());
    }

    public void setValid(int projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new RuntimeException("Project not found!"));

        List<Evaluation> evalutions = evaluationRepository.findByProjectId(project.getId());

        double points = 0;
        for(Evaluation evalution: evalutions){
            points += evalution.getPoints();
        }
        if(evalutions.size()>0){
            if(points/evalutions.size()>=50){
                project.setValid(true);
                projectRepository.save(project);
            }

        }

    }




    public List<Project> getProjectsByJwt(String jwt) throws Exception {
        Startup startup = startupService.getStartupByJwt(jwt);
        return projectRepository.findByStartupId(startup.getId());
    }


}


