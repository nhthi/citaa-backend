package com.citaa.citaa.repository;

import com.citaa.citaa.model.Expert;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartupRepository extends JpaRepository<Startup, Integer> {

    @Query("select s from Startup s where s.account.username=:username")
    public Startup findByUsername(@Param("username") String username);

    List<Startup> findTop5ByOrderByFullNameDesc();

    @Query("SELECT COUNT(c) " +
            "FROM Startup c " +
            "WHERE (:year = 0 or YEAR(c.account.createAt) = :year) AND (:month = 0 or MONTH(c.account.createAt) = :month)")
    long countStartupByYearAndMonth(@Param("year") int year, @Param("month") int month);



}
