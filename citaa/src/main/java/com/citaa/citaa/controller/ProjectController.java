package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.request.ProjectCreationRequest;
import com.citaa.citaa.response.ApiResponse;
import com.citaa.citaa.response.MessageResponse;
import com.citaa.citaa.service.*;
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
    UserService userService;
    @Autowired
    ConnectionService connectionService;

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
             @RequestParam(required = false) String status, @RequestParam(required = false) int pageNumber, @RequestParam(required = false) int pageSize,
            @RequestParam(defaultValue = "0") int year,
            @RequestParam(defaultValue = "-1") int countExpert
    ) {
        Page<Project> res = projectService.filterProject(
                fields,minCapital,status, pageNumber, pageSize,year,countExpert
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

    @GetMapping("/potential")
    public ResponseEntity<List<Project>> getProjectPotential() throws Exception {
        List<Project> projects = projectService.getProjectPotential();
        int length = projects.size() > 4 ? 4 : projects.size();
        return new ResponseEntity<>(projectService.getProjectPotential().subList(0,length), HttpStatus.OK);
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
    @GetMapping("/search")
    public ResponseEntity<Page<Project>> searchProject(@RequestParam int pageSize,
                                                       @RequestParam int pageNumber,
    @RequestParam String query) throws Exception {
        Page<Project> projects = projectService.searchProject(query,pageNumber,pageSize);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity<MessageResponse> createConnectionRequest(@RequestHeader("Authorization") String jwt, @RequestParam int projectId, @RequestBody ConnectionInfo connectionInfo) throws Exception {
        return new ResponseEntity<>(connectionService.createConnectionRequest(jwt, projectId,connectionInfo),HttpStatus.OK);
    }

    @PutMapping("/respond")
    public ResponseEntity<MessageResponse> respondToRequest(@RequestParam int requestId, @RequestParam(defaultValue = "PENDING") String status,@RequestParam String response) {
        return new ResponseEntity<>(connectionService.respondToRequest(requestId, status,response),HttpStatus.OK);

    }

    @GetMapping("/request")
    public ResponseEntity<Page<ConnectionRequest>> getConnectionByProjectId(@RequestParam int id,@RequestParam(defaultValue = "0") int pageSize,@RequestParam(defaultValue = "0") int pageNumber) throws Exception {
        return new ResponseEntity<>(connectionService.getConnectionRequestsByProjectId(id,pageSize,pageNumber),HttpStatus.OK);
    }
}
