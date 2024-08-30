package com.citaa.citaa.controller;

import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.request.ProjectCreationRequest;
import com.citaa.citaa.service.ProjectService;
import com.citaa.citaa.service.StartupService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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
}
