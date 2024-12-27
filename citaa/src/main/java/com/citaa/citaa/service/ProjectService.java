package com.citaa.citaa.service;

import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.CompetitionRepository;
import com.citaa.citaa.repository.EvaluationRepository;
import com.citaa.citaa.repository.ProjectRepository;
import com.citaa.citaa.repository.VoteRepository;
import com.citaa.citaa.request.ProjectCreationRequest;
import com.citaa.citaa.response.ApiResponse;
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
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    CompetitionRepository competitionRepository;

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
                        .potential(false)
                        .formationProject(request.getFormationProject())
                        .introduce(request.getIntroduce())
                        .startUpIdea(request.getStartUpIdea())
                        .founders(request.getFounders())
                        .phone(request.getPhone())
                        .email(request.getEmail())
                        .address(request.getAddress())
                        .linkWeb(request.getLinkWeb())
                        .businessModel(request.getBusinessModel())
                        .mainTechnology(request.getMainTechnology())
                        .pitchDeck(request.getPitchDeck())
                        .businessRegistrationCertificate(request.getBusinessRegistrationCertificate())
                        .patent(request.getPatent())
                        .greenSustainableElement(request.getGreenSustainableElement())
                .build());
    }

    public void setValid(int projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new RuntimeException("Project not found!"));
        double avg = calculateAveragePoints(projectId);
        project.setAvg(avg);
        if(avg>=80)
            project.setPotential(true);
        else
            project.setPotential(false);

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

    public List<Project> getProjectPotential() throws Exception {
        return projectRepository.getTopProjectPotential();
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

    public Project verifyProject(int projectId, String jwt) throws Exception {
        Project project = findProjectById(projectId);
        User user = userService.findByJwt(jwt);
        project.setStatus("VALID");
        return projectRepository.save(project);
    }

    public Page<Project> filterProject(List<String> fields, double minCapital, String status, int pageNumber, int pageSize,int year,int countExpert) {
        List<Project> projects = projectRepository.filterProjects(minCapital, status,year,countExpert);

        if (fields != null && !fields.isEmpty()) {
            projects = projects.stream()
                    .filter(project -> fields.stream().anyMatch(field -> field.equalsIgnoreCase(project.getField())))
                    .collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), projects.size());

        // Kiểm tra startIndex và endIndex hợp lệ
        if (startIndex > endIndex || startIndex > projects.size()) {
            return Page.empty(pageable);
        }

        List<Project> pageContent = projects.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageable, projects.size());
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

    public Page<Project> searchProject(String query, int pageNumber, int pageSize) throws Exception {
        List<Project> projects = projectRepository.searchProject(query);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), projects.size());

        List<Project> pageContent = projects.subList(startIndex, endIndex);
        Page<Project> filteredProject = new PageImpl<>(pageContent, pageable, projects.size());

        return filteredProject;
    }

    public long countProject(){
        return projectRepository.count();
    }

    public List<Object[]> getTop3StartupsWithMostProjects() {
        List<Object[]> results = projectRepository.findTopStartupsWithMostProjects();
        return results.stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public ApiResponse deleteProjectById(int id) throws Exception {
        ApiResponse res = new ApiResponse();
        Project project = getProjectById(id);
        List<Vote> votes = voteRepository.findVoteByProjectId(project.getId());
        for(Vote vote: votes){
            voteRepository.delete(vote);
        }
        List<Competition> competitions = competitionRepository.findAllByProjectsContaining(project);
        for (Competition competition : competitions) {
            competition.getProjects().remove(project);
            competitionRepository.save(competition);
        }
        projectRepository.delete(project);
        res.setMessage("Xóa thành công");
        res.setStatus(200);
        return res;
    }

}


