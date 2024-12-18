package com.citaa.citaa.repository;

import com.citaa.citaa.model.Vote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Query("select v from Vote v where v.competitionId=:competitionId and v.projectId=:projectId")
    public List<Vote> findVoteByCompetitionIdandProjectId(int competitionId, int projectId);

    @Query("select v from Vote v where v.competitionId=:competitionId")
    public List<Vote> findVoteByCompetitionId(int competitionId);

    @Query("SELECT v.competitionId, COUNT(v) as voteCount FROM Vote v GROUP BY v.competitionId ORDER BY voteCount DESC")
    List<Object[]> findTopCompetitionsByVoteCount(Pageable pageable);
}
