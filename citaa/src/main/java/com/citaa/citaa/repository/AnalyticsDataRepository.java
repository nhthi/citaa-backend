package com.citaa.citaa.repository;

import com.citaa.citaa.model.AnalyticsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsDataRepository extends JpaRepository<AnalyticsData, Long> {

    @Query("Select a from AnalyticsData a where (:year = 0 or year(a.date) = :year) and (:month = 0 or month(a.date) = :month)")
    List<AnalyticsData> findByYearAndMonth(int year, int month);
}
