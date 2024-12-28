package com.citaa.citaa.repository;

import com.citaa.citaa.model.Investor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Integer> {

    List<Investor> findTop5ByOrderByExperienceYearsDesc();

    @Query("SELECT COUNT(c) " +
            "FROM Investor c " +
            "WHERE (:year = 0 or YEAR(c.account.createAt) = :year) AND (:month = 0 or MONTH(c.account.createAt) = :month)")
    long countInvestorByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
