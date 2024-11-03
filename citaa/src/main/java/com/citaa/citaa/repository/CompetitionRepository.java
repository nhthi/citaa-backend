package com.citaa.citaa.repository;

import com.citaa.citaa.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
}
