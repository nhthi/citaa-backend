package com.citaa.citaa.repository;

import com.citaa.citaa.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    @Query("Select e from Evaluation e where e.projectId =:projectId")
    public List<Evaluation> findByProjectId(@Param("projectId") int projectId);


    @Query("Select e from Evaluation e where e.expert.id =:expertId")
    public List<Evaluation> findByExpertId(@Param("expertId") int expertId);

    @Query("SELECT p.expert, COUNT(p) as evalutionCount FROM Evaluation p where (:year = 0 or YEAR(p.createAt) = :year) AND (:month = 0 or MONTH(p.createAt) = :month) GROUP BY p.expert ORDER BY evalutionCount DESC")
    List<Object[]> findTopExpertMostProjects(int year, int month);

    @Query("SELECT COUNT(DISTINCT e.projectId) FROM Evaluation e WHERE e.expert.id = :expertId")
    Long countDistinctProjectByExpert(int expertId);

    @Query("SELECT AVG(e.points) FROM Evaluation e WHERE e.expert.id = :expertId")
    Double findAveragePointsByExpert(int expertId);
}
