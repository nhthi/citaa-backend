package com.citaa.citaa.repository;

import com.citaa.citaa.model.CompetitionRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRankingRepository extends JpaRepository<CompetitionRanking, Integer> {
}
