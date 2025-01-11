package com.citaa.citaa.repository;

import com.citaa.citaa.model.ConnectionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionInfoRepository extends JpaRepository<ConnectionInfo, Integer> {
}
