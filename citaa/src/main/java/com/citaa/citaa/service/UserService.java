package com.citaa.citaa.service;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.*;
import com.citaa.citaa.request.UpdateUserRequest;
import com.citaa.citaa.response.EvaluationManagementResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    StartupRepository startupRepository;
    @Autowired
    ExpertRepository expertRepository;
    @Autowired
    InvestorRepository investorRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findByJwt(String jwt) throws Exception {
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }

    public User findById(int id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found!"));
        switch (user.getAccount().getRole()) {
            case "ROLE_STARTUP" -> {
                Startup profile = startupRepository.findById(id).get();
                profile.getAccount().setUsername(null);
                profile.getAccount().setPassword(null);
                return profile;
            }
            case "ROLE_EXPERT" -> {
                Expert profile = expertRepository.findById(id).get();
                profile.getAccount().setUsername(null);
                profile.getAccount().setPassword(null);
                return profile;
            }
            case "ROLE_INVESTOR" -> {
                Investor profile = investorRepository.findById(id).get();
                profile.getAccount().setUsername(null);
                profile.getAccount().setPassword(null);
                return profile;
            }
        }
        return user;
    }

    public User updateUser(String jwt, UpdateUserRequest req) throws Exception {
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User updateUser = userRepository.findByUsername(username);

        if (updateUser == null) {
            throw new Exception("User not found");
        }
        if (req.getFullName() != null) {
            updateUser.setFullName(req.getFullName());
        }
        if (req.getAddress() != null) {
            updateUser.setAddress(req.getAddress());
        }
        if (req.getEmail() != null) {
            updateUser.setEmail(req.getEmail());
        }
        if (req.isGender() != updateUser.isGender()) {
            updateUser.setGender(req.isGender());
        }
        if (req.getDob() != null) {
            updateUser.setDob(req.getDob());
        }
        if (req.getAvatar() != null) {
            updateUser.setAvatar(req.getAvatar());
        }
        if (req.getBio() != null) {
            updateUser.setBio(req.getBio());
        }
        if (req.getCoverPhoto() != null) {
            updateUser.setCoverPhoto(req.getCoverPhoto());
        }

        if (req.getFields() != null) {
            updateUser.setFields(req.getFields());
        }
        userRepository.save(updateUser);

        switch (updateUser.getAccount().getRole()) {
            case "ROLE_STARTUP" -> {
                Startup updateStartup = startupRepository.findById(updateUser.getId())
                        .orElseThrow(() -> new Exception("Startup not found!"));
                if (req.getStudentId() != null) {
                    updateStartup.setStudentId(req.getStudentId());
                }
                if (req.getCohort() != null) {
                    updateStartup.setCohort(req.getCohort());
                }
                if (req.getCollege() != null) {
                    updateStartup.setCollege(req.getCollege());
                }
                startupRepository.save(updateStartup);
                return updateStartup;
            }
            case "ROLE_EXPERT" -> {
                Expert updateExpert = expertRepository.findById(updateUser.getId())
                        .orElseThrow(() -> new Exception("Expert not found!"));
                if (req.getCollege() != null) {
                    updateExpert.setCollege(req.getCollege());
                }

                if (req.getEducation() != null) {
                    updateExpert.setEducation(req.getEducation());
                }
                if (req.getCertifications() != null) {
                    updateExpert.setCertifications(req.getCertifications());
                }
                if (req.getExperienceYears() != updateExpert.getExperienceYears()) {
                    updateExpert.setExperienceYears(req.getExperienceYears());
                }
                expertRepository.save(updateExpert);
                return updateExpert;
            }
            case "ROLE_INVESTOR" -> {
                Investor updateInvestor = investorRepository.findById(updateUser.getId())
                        .orElseThrow(() -> new Exception("Investor not found!"));
                if (req.getRiskTolerance() != null) {
                    updateInvestor.setRiskTolerance(req.getRiskTolerance());
                }

                if (req.getCompanyName() != null) {
                    updateInvestor.setCompanyName(req.getCompanyName());
                }
                if (req.getInvestmentAmount() != 0) {
                    updateInvestor.setInvestmentAmount(req.getInvestmentAmount());
                }
                if (req.getExperienceYears() != updateInvestor.getExperienceYears()) {
                    updateInvestor.setExperienceYears(req.getExperienceYears());
                }
                investorRepository.save(updateInvestor);
                return updateInvestor;
            }
        }

        return updateUser;
    }

    public Expert addProjectToExpert(int projectId, int expertId) throws Exception {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new Exception("Expert not found with id: "+expertId));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("Project not found with id: "+projectId));
        expert.getProjects().add(project);
        return expertRepository.save(expert);
    }

    public List<Project> getProjectByExpertId(int expertId) throws Exception {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new Exception("Expert not found with id: "+expertId));
        return expert.getProjects();
    }

    public Page<Startup> getAllStartups(int pageSize, int pageNumber){
        List<Startup> startups = startupRepository.findAll();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), startups.size());
        List<Startup> pageContent = startups.subList(startIndex, endIndex);
        Page<Startup> filteredStartup = new PageImpl<>(pageContent, pageable, startups.size());
        return filteredStartup;
    }

    public Page<Expert> getAllExpert(int pageSize, int pageNumber){
        List<Expert> experts = expertRepository.findAll();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), experts.size());
        List<Expert> pageContent = experts.subList(startIndex, endIndex);
        Page<Expert> filteredExpert = new PageImpl<>(pageContent, pageable, experts.size());
        return filteredExpert;
    }

    public Page<Investor> getAllInvestor(int pageSize, int pageNumber){
        List<Investor> investors = investorRepository.findAll();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), investors.size());
        List<Investor> pageContent = investors.subList(startIndex, endIndex);
        Page<Investor> filteredInvestor = new PageImpl<>(pageContent, pageable, investors.size());
        return filteredInvestor;
    }

    public List<EvaluationManagementResponse> getEvaluateManagement(int expertId) throws Exception {
        List<Evaluation> evaluations = evaluationRepository.findByExpertId(expertId);
        List<EvaluationManagementResponse> evaluationManagementResponses = new ArrayList<>();
        System.out.println(evaluations.size());
        for (Evaluation evaluation : evaluations) {
            Project pro = projectRepository.findById(evaluation.getProjectId()).orElseThrow(()-> new Exception("Project not found with id: "+evaluation.getProjectId()));
            EvaluationManagementResponse item = new EvaluationManagementResponse();
            List<String> fields = new ArrayList<>();
            fields.add(pro.getField());
            List<String> founderNames = new ArrayList<>();
            for ( Founder founder: pro.getFounders()){
                founderNames.add(founder.getName());
            }
            evaluationManagementResponses.add(EvaluationManagementResponse.builder()
                    .fullName(evaluation.getExpert().getFullName())
                    .year(evaluation.getCreateAt().getYear())
                    .projectName(pro.getName())
                    .fields(fields)
                    .founderNames(founderNames)
                    .startupName(pro.getStartup().getFullName())
                    .build());
        }
        return evaluationManagementResponses;
    }

    public List<Investor> getTop5Investor(){
        return investorRepository.findTop5ByOrderByInvestmentAmountDesc();
    }
}
