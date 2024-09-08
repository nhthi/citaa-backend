package com.citaa.citaa.repository;

import com.citaa.citaa.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    @Query("Select e from Evaluation e where e.project.id =:projectId")
    public List<Evaluation> findByProjectId(@Param("projectId") int projectId);

}