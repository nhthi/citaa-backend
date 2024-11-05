package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.model.User;
import com.citaa.citaa.request.ProjectCreationRequest;
import com.citaa.citaa.response.ApiResponse;
import com.citaa.citaa.service.EvaluationService;
import com.citaa.citaa.service.ProjectService;
import com.citaa.citaa.service.StartupService;
import com.citaa.citaa.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api/project")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    StartupService startupService;
    @Autowired
    EvaluationService evaluationService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserService userService;


    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectCreationRequest request, @RequestHeader("Authorization") String jwt) throws Exception {
        Startup startUp = startupService.getStartupByJwt(jwt);

        Project createProject = projectService.createProject(request, startUp);
        return new ResponseEntity<>(createProject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getStartupProjects(@RequestHeader("Authorization") String jwt) throws Exception {
        List<Project> projects = projectService.getProjectsByJwt(jwt);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    @GetMapping("/filter")
    public ResponseEntity<Page<Project>> findProjectByCategoryHandler(
            @RequestParam(required = false) List<String> fields, @RequestParam(required = false) double minCapital,
            @RequestParam(required = false) double maxCapital, @RequestParam(required = false) String status, @RequestParam(required = false) int pageNumber, @RequestParam(required = false) int pageSize
    ) {
        System.out.println("status: "+status);
        Page<Project> res = projectService.filterProject(
                fields,minCapital,maxCapital,status, pageNumber, pageSize
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/startup/{id}")
    public ResponseEntity<List<Project>> getStartupProjectsById(@RequestHeader("Authorization") String jwt, @PathVariable("id") int id) throws Exception {
        User user = userService.findByJwt(jwt);
        List<Project> projects = projectService.getProjectsByStartupId(id);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/expert/{id}")
    public ResponseEntity<Page<Project>> getExpertProjectsById( @PathVariable("id") int id,@RequestParam(required = false) int pageNumber, @RequestParam(required = false) int pageSize) throws Exception {
        Page<Project> projects = projectService.getProjectByExpertId(id, pageNumber, pageSize);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PutMapping("/valid/{projectId}")
    public ResponseEntity<String> setValid(@PathVariable int projectId){
        projectService.setValid(projectId);
        return new ResponseEntity<>("Set valid successfully!", HttpStatus.OK);
    }

    @PutMapping("like/{projectId}")
    public ResponseEntity<Project> likePostHandler(@PathVariable int projectId,
                                                @RequestHeader("Authorization")String jwt) throws Exception {
        User reqUser = userService.findByJwt(jwt);
        Project project = projectService.likeProject(projectId,jwt);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectsById(@RequestHeader("Authorization") String jwt, @PathVariable("id") int id) throws Exception {
        User user = userService.findByJwt(jwt);
        Project project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody ProjectCreationRequest[] reqs,@RequestHeader("Authorization")String jwt) throws Exception {
        Startup startUp = startupService.getStartupByJwt(jwt);
        for(ProjectCreationRequest req : reqs){
            projectService.createProject(req,startUp);
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Created project successfully");
        res.setStatus(200);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

}
