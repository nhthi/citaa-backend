package com.citaa.citaa.response;

import com.citaa.citaa.model.Project;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminProjectOverview {


    long agricultureProject = 0;
    long aquacultureProject = 0;
    long technologyProject = 0;
    long otherProject = 0;

    long validProjects = 0;
    long unvalidProjects = 0;

    long connectionProjects = 0;

    List<Project> topPointProjects = new ArrayList<>();
    List<Project> topReactProjects = new ArrayList<>();
    List<Object[]> topConnectionProjects = new ArrayList<>();
}
