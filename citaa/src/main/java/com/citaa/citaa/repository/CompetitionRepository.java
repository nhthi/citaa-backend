package com.citaa.citaa.repository;

import com.citaa.citaa.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

    @Query("SELECT c FROM Competition c WHERE " +
            "(:year IS NULL OR YEAR(c.startAt) = :year) AND " +
            "(:field = 'all' OR :field MEMBER OF c.fields) AND " +
            "(:status IS NULL OR " +
            "(:status = 'ongoing' AND c.startAt <= CURRENT_DATE AND c.endAt >= CURRENT_DATE) OR " +
            "(:status = 'ended' AND c.endAt < CURRENT_DATE) OR " +
            "(:status = 'upcoming' AND c.startAt > CURRENT_DATE))")
    public List<Competition> filterCompetition(String year, String field, String status );
}
