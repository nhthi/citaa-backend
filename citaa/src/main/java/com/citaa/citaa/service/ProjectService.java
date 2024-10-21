package com.citaa.citaa.service;

import com.citaa.citaa.model.Evaluation;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.EvaluationRepository;
import com.citaa.citaa.repository.ProjectRepository;
import com.citaa.citaa.request.ProjectCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    StartupService startupService;
    @Autowired
    EvaluationRepository evaluationRepository;
    @Autowired
    UserService userService;


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

    public Project findProjectById(int id) throws Exception {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty())
            throw new Exception("Project not found : "+ id);
        return project.get();
    }

    public Project likeProject(int projectId, String jwt) throws Exception {
        Project project = findProjectById(projectId);
        User user = userService.findByJwt(jwt);
        if(project.getReacts().contains(user)){
            project.getReacts().remove(user);
        }else project.getReacts().add(user);
        return projectRepository.save(project);
    }

//    public Post saveProject(Integer postId, Integer userId) throws Exception {
//        Post post = findPostById(postId);
//        User user = userService.findUserById(userId);
//        if(post.getLikedPost().contains(user)){
//            post.getLikedPost().remove(user);
//        }else post.getLikedPost().add(user);
//
//        return postReository.save(post);
//    }

    public Page<Project> filterProject( List<String> fields,  double minCapital, double maxCapital, String status,int pageNumber, int pageSize) {
        boolean valid = false;
        if(status.equals("valid")){
            valid = true;
        }
        List<Project> projects = projectRepository.filterProjects(minCapital, maxCapital, status,valid);

        if (fields!=null && !fields.isEmpty()) {
            projects = projects.stream().
                    filter(
                            project -> fields.stream().anyMatch((field -> field.equalsIgnoreCase(project.getField())))
                    ).collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), projects.size());

        List<Project> pageContent = projects.subList(startIndex, endIndex);
        Page<Project> filteredProject = new PageImpl<>(pageContent, pageable, projects.size());

        return filteredProject;
    }

    public Page<Project> getProjectByExpertId(int projectId, int pageNumber, int pageSize) throws Exception {
        List<Project> projects = userService.getProjectByExpertId(projectId);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), projects.size());

        List<Project> pageContent = projects.subList(startIndex, endIndex);
        Page<Project> filteredProject = new PageImpl<>(pageContent, pageable, projects.size());

        return filteredProject;
    }
}


