package com.citaa.citaa.repository;

import com.citaa.citaa.model.EvaluationCompetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationCompetitionRepository extends JpaRepository<EvaluationCompetition, Integer> {
    @Query("SELECT COUNT(e) > 0 FROM EvaluationCompetition e WHERE e.competitionId = :competitionId AND e.judge.id = :judgeId AND e.projectId = :projectId")
    boolean existsByCompetitionIdAndJudgeIdAndProjectId(int competitionId, int judgeId, int projectId);

    @Query("select e from EvaluationCompetition e where e.judge.id = :judgeId and e.competitionId=:competitionId")
    List<EvaluationCompetition> findByCompetitionIdAndJudgeId(int competitionId,int judgeId);


    @Query("select e from EvaluationCompetition e where e.competitionId=:competitionId")
    List<EvaluationCompetition> findByCompetitionId(int competitionId);
}
