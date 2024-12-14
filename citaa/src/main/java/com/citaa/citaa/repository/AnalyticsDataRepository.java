package com.citaa.citaa.repository;

import com.citaa.citaa.model.AnalyticsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsDataRepository extends JpaRepository<AnalyticsData, Long> {
}
