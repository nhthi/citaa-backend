package com.citaa.citaa.repository;

import com.citaa.citaa.model.TimelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineEventRepository extends JpaRepository<TimelineEvent, Integer> {
}
