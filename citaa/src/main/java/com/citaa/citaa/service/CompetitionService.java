package com.citaa.citaa.service;

import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.CompetitionRepository;
import com.citaa.citaa.repository.TimelineEventRepository;
import com.citaa.citaa.repository.VoteRepository;
import com.citaa.citaa.request.CompetitionRequest;
import com.citaa.citaa.response.AdminCompetitionResponse;
import com.citaa.citaa.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private VoteRepository voteRepository;

    public Competition createCompetition(String jwt, CompetitionRequest req) throws Exception {
        User admin = userService.findByJwt(jwt);

        // Tạo đối tượng Competition mới
        Competition newCompetition = Competition.builder()
                .name(req.getName())
                .introduce(req.getIntroduce())
                .content(req.getDescription())
                .files(req.getFiles())
                .fields(req.getFields())
                .judges(req.getJudges())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .startAt(req.getStartAt())
                .endAt(req.getEndAt())
                .admin(admin) // Gán admin đã tìm thấy từ jwt
                .timelineEvents(new ArrayList<>())
                .first(req.getFirst())
                .second(req.getSecond())
                .third(req.getThird())
                // Khởi tạo danh sách timelineEvents
                .build();

        // Thiết lập mối quan hệ với các TimelineEvent
//        if (req.getTimelineEvents() != null) {
//            for (TimelineEvent event : req.getTimelineEvents()) {
//                event.setCompetition(newCompetition); // Thiết lập Competition cho từng event
//                newCompetition.getTimelineEvents().add(event); // Thêm event vào danh sách của Competition
//            }
//        }
        for(int i=0;i< req.getNumberOfStages();i++){
            TimelineEvent event = new TimelineEvent();
            event.setEventName(req.getStages().get(i));
            event.setDescription(req.getDescriptionStages().get(i));
            event.setEventTime(req.getDateStages().get(i));
            event.setCompetition(newCompetition);
            newCompetition.getTimelineEvents().add(event);
        }

        // Lưu Competition và các TimelineEvent liên quan
        return competitionRepository.save(newCompetition);
    }

    public Competition findCompetitionById(int id) throws Exception {
        Optional<Competition> competition = competitionRepository.findById(id);
        if(competition.isEmpty()){
            throw new Exception("Competition not found with id : "+id);
        }
        return competition.get();
    }


    public ApiResponse applyCompetition(String jwt, int idCompetition, int projectId) throws Exception {
        ApiResponse res = new ApiResponse();
        // Tìm User từ JWT
        User candidate = userService.findByJwt(jwt);

        // Kiểm tra nếu User tìm thấy
        if (candidate == null) {
            res.setStatus(404);
            res.setMessage("Không tìm thấy ứng viên");
            return res;
        }
        // Tìm Project theo ID
        Project project = projectService.findProjectById(projectId);


        // Kiểm tra nếu Project tồn tại
        if (project == null) {
            res.setStatus(404);
            res.setMessage("Không tìm thấy dự án");
            return res;
        }
        // Tìm Competition theo ID
        Competition competition = findCompetitionById(idCompetition);

        // Kiểm tra nếu Competition tồn tại
        if (competition == null) {
            res.setStatus(404);
            res.setMessage("Không tìm thấy cuộc thi");
            return res;
        }

        if(project.getStartup().getId() != candidate.getId()){
            res.setStatus(400);
            res.setMessage("Đây không phải dự án của bạn");
            return res;
        }


        // Kiểm tra nếu Candidate đã đăng ký vào Competition
        if (competition.getStartupAppliedTimes().containsKey(candidate.getId())) {
            res.setStatus(400);
            res.setMessage("Ứng viên đã tham gia cuộc thi này rồi");
            return res;
        }
        if (competition.getProjects().contains(project)) {
            res.setStatus(400);
            res.setMessage("Dự án này đã được đăng ký với cuộc thi này");
            return res;
        }
        // Ghi nhận thời gian đăng ký của Candidate
        competition.getStartupAppliedTimes().put( candidate.getId(), LocalDateTime.now());

        // Thêm Project vào danh sách Projects của Competition
        competition.getProjects().add(project);

        // Lưu Competition sau khi cập nhật
        competitionRepository.save(competition);
        res.setStatus(200);
        res.setMessage("Đăng ký tham gia thành công");
        return res;
    }

    public ApiResponse voteForProject(String jwt, int competitionId, int projectId) throws Exception {

        ApiResponse res = new ApiResponse();

        User voter = userService.findByJwt(jwt);
        Competition competition = findCompetitionById(competitionId);
        // Tìm dự án trong cuộc thi
        Project project = projectService.findProjectById(projectId);

        if (!competition.getProjects().contains(project)) {
            res.setStatus(404);
            res.setMessage("Project not found in this competition.");
            return res;
        }

        // Kiểm tra nếu người dùng đã bình chọn cho dự án này trong cuộc thi này
        List<Vote> votes = voteRepository.findVoteByCompetitionIdandProjectId(competitionId, projectId);
        for(Vote vote: votes){
            if(vote.getUserId() == voter.getId()){
                voteRepository.delete(vote);
                res.setStatus(200);
                res.setMessage("Đã hủy bình chọn!");
                return res;
            }
        }
        List<Vote> voteCompetition = voteRepository.findVoteByCompetitionId(competitionId);
        for(Vote vote: voteCompetition){
            if(vote.getUserId() == voter.getId()){
                voteRepository.delete(vote);
            }
        }

        // Tạo một bình chọn mới
        Vote vote = Vote.builder()
                .userId(voter.getId())
                .projectId(project.getId())
                .competition(competition)
                .build();
        // Lưu bình chọn vào cơ sở dữ liệu
        voteRepository.save(vote);
        res.setStatus(200);
        res.setMessage("Bình chọn thành công!");
        return res;
    }

    public List<Vote> getVotesByCompetitionId(int competitionId) throws Exception {
        Competition competition = findCompetitionById(competitionId);
        return voteRepository.findVoteByCompetitionId(competitionId);
    }

    public Page<Competition> filterCompetition( int pageNumber, int pageSize,String year, String field, String status ) throws Exception {
        List<Competition> competitions = competitionRepository.filterCompetition(year,field,status);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), competitions.size());
        List<Competition> pageContent = competitions.subList(startIndex, endIndex);
        Page<Competition> filteredCompetition = new PageImpl<>(pageContent, pageable, competitions.size());
        return filteredCompetition;
    }

    public long countCompetition(){
        return competitionRepository.count();
    }

    public Page<Competition> filterAllCompetition( int pageNumber, int pageSize,int year, List<String> fields ) throws Exception {
        List<Competition> competitions = competitionRepository.filterAllCompetition(year);

        if (fields != null && !fields.isEmpty()) {
            competitions = competitions.stream()
                    .filter(competition -> competition.getFields().stream()
                            .anyMatch(field -> fields.stream()
                                    .anyMatch(inputField -> inputField.equalsIgnoreCase(field))
                            )
                    )
                    .collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), competitions.size());
        List<Competition> pageContent = competitions.subList(startIndex, endIndex);
        Page<Competition> filteredCompetition = new PageImpl<>(pageContent, pageable, competitions.size());
        return filteredCompetition;
    }

    public ApiResponse deleteCompetitionById(int id) throws Exception {
        ApiResponse res = new ApiResponse();
        Competition competition = findCompetitionById(id);
        List<Vote> votes = voteRepository.findVoteByCompetitionId(competition.getId());
        for(Vote vote: votes){
            voteRepository.delete(vote);
        }
        competitionRepository.delete(competition);
        res.setMessage("Xóa thành công");
        res.setStatus(200);
        return res;
    }

    public AdminCompetitionResponse getAdminCompetitionAnalysis(int year, int month) throws Exception {
        AdminCompetitionResponse res = new AdminCompetitionResponse();
        int onGoingCompetition = competitionRepository.filterCompetitionByStatus("ongoing").size();
        int endedCompetition = competitionRepository.filterCompetitionByStatus("ended").size();
        int upcomingCompetition = competitionRepository.filterCompetitionByStatus("upcoming").size();

        res.setOnGoingCompetition(onGoingCompetition);
        res.setEndedCompetition(endedCompetition);
        res.setUpcomingCompetition(upcomingCompetition);

        int agricultureCompetition = competitionRepository.findByField("Agriculture",year,month).size();
        int aquacultureCompetition = competitionRepository.findByField("Aquaculture",year,month).size();

        res.setAgricultureCompetition(agricultureCompetition);
        res.setAquacultureCompetition(aquacultureCompetition);

        List<String> fields = new ArrayList<>();

        int otherCompetition = competitionRepository.findByNotFields("Agriculture").size();

        Pageable pageable = PageRequest.of(0, 3); // Trang đầu tiên với 3 kết quả

        List<Competition> topJoin = competitionRepository.findTop3ByProjectsCount(pageable,year,month);
        List<Competition> topReward = new ArrayList<>();
        List<Object[]> topVote = voteRepository.findTopCompetitionsByVoteCount(pageable,year,month);

        res.setTopJoin(topJoin);
        res.setTopReward(topReward);
        res.setTopVote(topVote);

        return res;
    }

    public List<Competition> getCompetitionByJudge(String jwt,String status) throws Exception {
        User user = userService.findByJwt(jwt);
        return competitionRepository.filterByJudge(user,status);
    }
}
