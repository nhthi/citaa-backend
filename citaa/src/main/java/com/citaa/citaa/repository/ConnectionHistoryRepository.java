package com.citaa.citaa.repository;

import com.citaa.citaa.model.ConnectionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionHistoryRepository extends JpaRepository<ConnectionHistory, Integer> {
}
