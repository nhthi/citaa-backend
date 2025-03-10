package com.citaa.citaa.service;

import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.*;
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
    @Autowired
    ReactRepository reactRepository;
    @Autowired
    ExpertRepository expertRepository;
    @Autowired
    ConnectionRequestRepository connectionRequestRepository;

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
                        .status("UNVALID")
                .build());
    }

    public Project updateProject(ProjectCreationRequest request, int projectId) throws Exception {
        Project project = findProjectById(projectId);
        if(request.getName() != null){
            project.setName(request.getName());
        }
        if(request.getIntroduce() != null){
            project.setIntroduce(request.getIntroduce());
        }
        if(request.getFormationProject() != null){
            project.setFormationProject(request.getFormationProject());
        }

        if(request.getStartUpIdea()!=null){
            project.setStartUpIdea(request.getStartUpIdea());
        }
        if(request.getField() !=null){
            project.setField(request.getField());
        }

        if(request.getRealTotalCapital()>0){
            project.setRealTotalCapital(request.getRealTotalCapital());
        }
        if(request.getFiles() != null){
            project.setFiles(request.getFiles());
        }
        if(request.getFounders()!=null){
            project.setFounders(request.getFounders());
        }
        if(request.getPhone()!=null){
            project.setPhone(request.getPhone());
        }
        if(request.getEmail()!=null){
            project.setEmail(request.getEmail());
        }
        if(request.getAddress()!=null){
            project.setAddress(request.getAddress());
        }
        if(request.getLinkWeb()!=null){
            project.setLinkWeb(request.getLinkWeb());
        }
        if(request.getBusinessModel()!=null){
            project.setBusinessModel(request.getBusinessModel());
        }
        if(request.getMainTechnology()!=null){
            project.setMainTechnology(request.getMainTechnology());
        }
        if(request.getPitchDeck()!=null){
            project.setPitchDeck(request.getPitchDeck());
        }
        if(request.getBusinessRegistrationCertificate()!=null){
            project.setBusinessRegistrationCertificate(request.getBusinessRegistrationCertificate());
        }
        if(request.getPatent()!=null){
            project.setPatent(request.getPatent());
        }
        if(request.getGreenSustainableElement()!=null){
            project.setGreenSustainableElement(request.getGreenSustainableElement());
        }

        project.setUpdateAt(LocalDateTime.now());
        return projectRepository.save(project);

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

    public Page<Project> getProjectsByStartupId(int id,List<String> fields,String status,int pageNumber,int pageSize) throws Exception {
        List<Project> projects =  projectRepository.findByStartupIdAndStatus(id,status);
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

    public Project findProjectByIdAndValid(int id,String status) throws Exception {
        Project project = findProjectById(id);
        if(status.equalsIgnoreCase("VALID")){
            if(project.getStatus().equalsIgnoreCase("VALID"))
                return project;
            else return null;
        }
        return project;
    }

    public Project likeProject(int projectId, String jwt) throws Exception {
        Project project = findProjectById(projectId);
        User user = userService.findByJwt(jwt);

        if(project.getReactList().contains(user)){
            project.getReactList().remove(user);
        }else project.getReactList().add(user);

        Optional<React> existingReact = project.getReacts().stream()
                .filter(react -> react.getUser().equals(user))
                .findFirst();

        if (existingReact.isPresent()) {
            reactRepository.delete(existingReact.get());
            project.getReacts().remove(existingReact.get());
        } else {
            React react = React.builder()
                    .user(user)
                    .project(project)
                    .createAt(LocalDateTime.now())
                    .build();
            project.getReacts().add(react);
        }

        return projectRepository.save(project);
    }

    public Project verifyProject(int projectId, String jwt) throws Exception {
        Project project = findProjectById(projectId);
        User user = userService.findByJwt(jwt);
        project.setStatus("VALID");
        project.setValidAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public Page<Project> filterProject(List<String> fields, double minCapital, String status, int pageNumber, int pageSize,int year,int countExpert,String potential) {
        List<Project> projects = projectRepository.filterProjects(minCapital, status,year,countExpert);

        if(potential.equalsIgnoreCase("isPotential")){
            projects = projects.stream()
                    .filter(Project::isPotential)
                    .collect(Collectors.toList());
        }

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

    public Page<Project> getProjectByExpertId(List<String> fields, double minCapital,int projectId, int pageNumber, int pageSize) throws Exception {
        List<Project> projects = userService.getProjectByExpertId(projectId);


        projects = projects.stream()
                .filter(project -> project.getRealTotalCapital() >= minCapital).collect(Collectors.toList());

        if (fields != null && !fields.isEmpty()) {
            projects = projects.stream()
                    .filter(project -> fields.stream().anyMatch(field -> field.equalsIgnoreCase(project.getField())))
                    .collect(Collectors.toList());
        }



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

    public List<Object[]> getTop3StartupsWithMostProjects(int year,int month) {
        List<Object[]> results = projectRepository.findTopStartupsWithMostProjects(year,month);
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

        for (Expert expert : project.getExperts()) {
            expert.getProjects().remove(project);
            expertRepository.save(expert);
        }

        List<Evaluation> evaluations = evaluationRepository.findByProjectId(id);
        evaluationRepository.deleteAll(evaluations);

        List<ConnectionRequest> cons = connectionRequestRepository.findByProjectId(project.getId());
        connectionRequestRepository.deleteAll(cons);

        projectRepository.delete(project);
        res.setMessage("Xóa thành công");
        res.setStatus(200);
        return res;
    }

}


