package com.citaa.citaa.repository;

import com.citaa.citaa.model.Expert;
import com.citaa.citaa.model.Investor;
import com.citaa.citaa.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {
    List<Expert> findTop5ByOrderByFullNameAsc();

    @Query("SELECT COUNT(c) " +
            "FROM Expert c " +
            "WHERE c.account.role='ROLE_EXPERT' and  (:year = 0 or YEAR(c.account.createAt) = :year) AND (:month = 0 or MONTH(c.account.createAt) = :month)")
    long countExpertByYearAndMonth(@Param("year") int year, @Param("month") int month);

}
