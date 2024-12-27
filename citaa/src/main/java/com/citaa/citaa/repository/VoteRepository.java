package com.citaa.citaa.repository;

import com.citaa.citaa.model.Vote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Query("select v from Vote v where v.competition.id=:competitionId and v.projectId=:projectId")
    public List<Vote> findVoteByCompetitionIdandProjectId(int competitionId, int projectId);

    @Query("select v from Vote v where v.competition.id=:competitionId")
    public List<Vote> findVoteByCompetitionId(int competitionId);

    @Query("select v from Vote v where v.projectId=:projectId")
    public List<Vote> findVoteByProjectId(int projectId);

    @Query("SELECT v.competition, COUNT(v) as voteCount FROM Vote v GROUP BY v.competition ORDER BY voteCount DESC")
    List<Object[]> findTopCompetitionsByVoteCount(Pageable pageable);
}
