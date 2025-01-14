package com.citaa.citaa.service;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.*;
import com.citaa.citaa.request.UpdateUserRequest;
import com.citaa.citaa.response.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    ReactRepository reactRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FeedbackRepository feedbackRepository;

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

    public User findUserChatById(int id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found!"));
        return user;
    }

    public User findById(int id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found!"));
        switch (user.getAccount().getRole()) {
            case "ROLE_STARTUP" -> {
                Startup profile = startupRepository.findById(id).get();
                return profile;
            }
            case "ROLE_EXPERT" -> {
                Expert profile = expertRepository.findById(id).get();
                return profile;
            }
            case "ROLE_INVESTOR" -> {
                Investor profile = investorRepository.findById(id).get();
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
                if (req.getExperienceYears() != updateInvestor.getExperienceYears()) {
                    updateInvestor.setExperienceYears(req.getExperienceYears());
                }
                investorRepository.save(updateInvestor);
                return updateInvestor;
            }
        }

        return updateUser;
    }

    public Project addProjectToExpert(int projectId, int[] expertIds) throws Exception {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("Project not found with id: " + projectId));
        for (int expertId : expertIds) {
            Expert expert = expertRepository.findById(expertId)
                    .orElseThrow(() -> new Exception("Expert not found with id: " + expertId));
            if (project.getCountExpert() == 3) {
                throw new Exception("Đã đủ số lượng chuyên gia");
            }
            project.setCountExpert(project.getCountExpert() + 1);
            project.getExperts().add(expert);
            expert.getProjects().add(project);
            expertRepository.save(expert);
        }
        return projectRepository.save(project);
    }

    public List<Project> getProjectByExpertId(int expertId) throws Exception {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new Exception("Expert not found with id: " + expertId));
        return expert.getProjects();
    }

    public Page<Startup> getAllStartups(int pageSize, int pageNumber) {
        List<Startup> startups = startupRepository.findAll();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), startups.size());
        List<Startup> pageContent = startups.subList(startIndex, endIndex);
        Page<Startup> filteredStartup = new PageImpl<>(pageContent, pageable, startups.size());
        return filteredStartup;
    }

    public Page<Expert> getAllExpert(int pageSize, int pageNumber) {
        List<Expert> experts = expertRepository.findAll();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), experts.size());
        List<Expert> pageContent = experts.subList(startIndex, endIndex);
        Page<Expert> filteredExpert = new PageImpl<>(pageContent, pageable, experts.size());
        return filteredExpert;
    }

    public Page<Investor> getAllInvestor(int pageSize, int pageNumber) {
        List<Investor> investors = investorRepository.findAll();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), investors.size());
        List<Investor> pageContent = investors.subList(startIndex, endIndex);
        Page<Investor> filteredInvestor = new PageImpl<>(pageContent, pageable, investors.size());
        return filteredInvestor;
    }

    public Page<User> getAllAdmin(int pageSize, int pageNumber) {
        List<User> admins = userRepository.findAllAdmin();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), admins.size());
        List<User> pageContent = admins.subList(startIndex, endIndex);
        Page<User> filteredStartup = new PageImpl<>(pageContent, pageable, admins.size());
        return filteredStartup;
    }

    public List<EvaluationManagementResponse> getEvaluateManagement(int expertId) throws Exception {
        List<Evaluation> evaluations = evaluationRepository.findByExpertId(expertId);
        List<EvaluationManagementResponse> evaluationManagementResponses = new ArrayList<>();
        for (Evaluation evaluation : evaluations) {
            Project pro = projectRepository.findById(evaluation.getProjectId()).orElseThrow(() -> new Exception("Project not found with id: " + evaluation.getProjectId()));
            EvaluationManagementResponse item = new EvaluationManagementResponse();
            List<String> fields = new ArrayList<>();
            fields.add(pro.getField());
            List<String> founderNames = new ArrayList<>();
            for (Founder founder : pro.getFounders()) {
                founderNames.add(founder.getName());
            }
            evaluationManagementResponses.add(EvaluationManagementResponse.builder()
                    .fullName(evaluation.getExpert().getFullName())
                    .createAt(evaluation.getCreateAt())
                    .projectName(pro.getName())
                    .fields(fields)
                    .founderNames(founderNames)
                    .startupName(pro.getStartup().getFullName())
                    .projectId(evaluation.getProjectId())
                    .build());
        }
        return evaluationManagementResponses;
    }

    public List<User> getTop5Investor() {
        return investorRepository.findTop5ByOrderByExperienceYearsDesc().stream()
                .map(investor -> (User) investor)
                .collect(Collectors.toList());
    }

    public List<User> getTop5Expert() {
        return expertRepository.findTop5ByOrderByFullNameAsc().stream()
                .map(expert -> (User) expert)
                .collect(Collectors.toList());
    }

    public List<User> getTop5Startup() {
        return startupRepository.findTop5ByOrderByFullNameDesc().stream()
                .map(startup -> (User) startup)
                .collect(Collectors.toList());
    }

    public List<User> getTopProfile(String role) {
        switch (role) {
            case "Expert":
                return getTop5Expert();
            case "Startup":
                return getTop5Startup();
            case "Investor":
                return getTop5Investor();
        }
        return null;
    }

    public ApiResponse changePassword(String jwt, String currentPassword, String newPassword) throws Exception {
        User user = findByJwt(jwt);
        ApiResponse res = new ApiResponse();

        if (user == null) {
            res.setMessage("User not found");
            res.setStatus(500);
            return res;
        }
        ;

        if (!passwordEncoder.matches(currentPassword, user.getAccount().getPassword())) {
            res.setMessage("Mật khẩu hiện tại không chính xác");
            res.setStatus(500);
            return res;
        }

        user.getAccount().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        res.setMessage("Đổi mật khẩu thành công");
        res.setStatus(200);
        return res;
    }

    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email:  " + email);
        }
        return user;
    }

    public long countExpert() {
        return expertRepository.count();
    }

    public long countStartup() {
        return startupRepository.count();
    }

    public long countInvestor() {
        return investorRepository.count();
    }

    public User updateStatus(int userId, String status) throws Exception {
        User user = findById(userId);
        user.getAccount().setStatus(status);
        return userRepository.save(user);
    }


    public long countProjectsByExpert(int expertId) {
        return evaluationRepository.countDistinctProjectByExpert(expertId);
    }

    public double averagePointOfExpert(int expertId) {
        Double averagePoints = evaluationRepository.findAveragePointsByExpert(expertId);
        return (averagePoints != null) ? averagePoints : 0.0;
    }

    public long countProjectsByStartup(int startupId) {
        return projectRepository.countDistinctProjectByStartupId(startupId);
    }

    public long countCompetitionByStartupId(int startupId) {
        return competitionRepository.countCompetitionsByStartupId(startupId);
    }

    public double sumTotalCapital(int startupId) {
        Double sum = projectRepository.sumTotalCapital(startupId);
        return (sum != null) ? sum : 0;
    }

    public long countJudgingCompetitionsByExpertId(int expertId) {
        return competitionRepository.countJudgingCompetitionsByExpertId(expertId);
    }

    public ProfileResponse getProfileStatistical(int userId) throws Exception {
        User user = findById(userId);
        ProfileResponse res = new ProfileResponse();
        switch (user.getAccount().getRole()) {
            case "ROLE_EXPERT":
                res.setAveragePoints(averagePointOfExpert(user.getId()));
                res.setCountProjectExpert(countProjectsByExpert(user.getId()));
                res.setCountDoJudgeExpert(countJudgingCompetitionsByExpertId(userId));
            case "ROLE_STARTUP":
                res.setCountProjectStartup(countProjectsByStartup(userId));
                res.setCountCompetitionStartup(countCompetitionByStartupId(userId));
                res.setTotalCapitalStartup(sumTotalCapital(userId));
        }
        return res;
    }

    public List<React> findReactByJwt(String jwt) throws Exception {
        User user = findByJwt(jwt);
        return reactRepository.findAllByUserId(user.getId());
    }


    public AdminOverview getAdminOverview(String jwt, int year, int month) throws Exception {
        findByJwt(jwt);
        AdminOverview res = new AdminOverview();
        res.setCountComment(commentRepository.countCommentsByMonthAndYear(year, month));
        res.setCountFeedback(feedbackRepository.countFeedbacksByMonthAndYear(year, month));
        res.setCountReplyFeedback(feedbackRepository.countReplyFeedbacksByMonthAndYear(year, month));
        res.setNewAccount(accountRepository.countAccount(year, month));
        return res;
    }

    public StatisticalResponse getStatisticalResponse(int year, int month) {
        long countExpert = expertRepository.countExpertByYearAndMonth(year, month);
        long countStartup = startupRepository.countStartupByYearAndMonth(year, month);
        long countInvestor = investorRepository.countInvestorByYearAndMonth(year, month);
        long countProject = projectRepository.countProjectByYearAndMonth(year, month);
        long countCompetition = competitionRepository.countCompetitionByYearAndMonth(year, month);
        StatisticalResponse response = new StatisticalResponse();
        response.setCountExpert(countExpert);
        response.setCountStartup(countStartup);
        response.setCountInvestor(countInvestor);
        response.setCountProject(countProject);
        response.setCountCompetition(countCompetition);
        return response;
    }

    public AdminProjectOverview getAdminProjectOverview(String jwt, int year, int month) throws Exception {
        findByJwt(jwt);


        AdminProjectOverview res = new AdminProjectOverview();

        Pageable pageable = PageRequest.of(0, 3);

        long agricultureProject = projectRepository.countProjectByYearAndMonthAndField(year, month, "Agriculture");
        long aquacultureProject = projectRepository.countProjectByYearAndMonthAndField(year, month, "Aquaculture");
        long technologyProject = projectRepository.countProjectByYearAndMonthAndField(year, month, "Technology");
        long otherProject = projectRepository.countProjectByYearAndMonth(year, month) - agricultureProject - aquacultureProject - technologyProject;

        long validProjects = projectRepository.countProjectByYearAndMonthAndStatus(year, month, "VALID");
        long unvalidProjects = projectRepository.countProjectByYearAndMonthAndStatus(year, month, "UNVALID");
        ;

        List<Project> topPointProjects = projectRepository.findTop3ByAvg(pageable, year, month);
        List<Project> topReactProjects = projectRepository.findTop3ByReact(pageable, year, month);

        res.setAgricultureProject(agricultureProject);
        res.setAquacultureProject(aquacultureProject);
        res.setTechnologyProject(technologyProject);
        res.setOtherProject(otherProject);
        res.setValidProjects(validProjects);
        res.setUnvalidProjects(unvalidProjects);
        res.setTopPointProjects(topPointProjects);
        res.setTopReactProjects(topReactProjects);

        return res;
    }

    public MessageResponse sendRequestValidUser(String jwt) throws Exception {
        User user = findByJwt(jwt);
        user.setRequestAt(LocalDateTime.now());
        user.setValid("PENDING");
        userRepository.save(user);
        MessageResponse res = new MessageResponse();
        res.setStatus(200);
        res.setMessage("Đã gửi yêu cầu duyệt tài khoản.");
        return res;
    }

    public MessageResponse validUser(int userId) throws Exception {
        User user = findById(userId);
        user.setValidAt(LocalDateTime.now());
        user.setValid("VALID");
        userRepository.save(user);
        MessageResponse res = new MessageResponse();
        res.setStatus(200);
        res.setMessage("Đã duyệt tài khoản.");
        return res;
    }

    public Page<User> findByProvinceAndFieldAndQuery(String province, String role, String fieldFilter, String query,String valid, int pageSize, int pageNumber) {
        List<User> users = userRepository.findByProvinceAndFieldAndQuery(province, role, query,valid);

        // Lọc theo fieldFilter nếu có
        if (fieldFilter != null && !fieldFilter.isEmpty() && !fieldFilter.equalsIgnoreCase("0")) {
            users = users.stream()
                    .filter(user -> user.getFields().stream().anyMatch(field -> field.equalsIgnoreCase(fieldFilter)))
                    .collect(Collectors.toList());
        }

        // Tạo đối tượng Pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Xử lý logic phân trang
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageSize, users.size());

        // Kiểm tra nếu startIndex vượt quá kích thước danh sách
        if (startIndex >= users.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, users.size());
        }

        // Lấy danh sách con cho trang hiện tại
        List<User> pageContent = users.subList(startIndex, endIndex);

        // Trả về đối tượng Page
        return new PageImpl<>(pageContent, pageable, users.size());
    }


}
