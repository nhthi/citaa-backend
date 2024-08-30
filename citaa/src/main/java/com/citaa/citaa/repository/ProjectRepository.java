package com.citaa.citaa.repository;

import com.citaa.citaa.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p where p.startup.id =:startupId")
    public List<Project> findByStartupId(@Param("startupId") int startupId);
}
