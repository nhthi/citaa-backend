package com.citaa.citaa.repository;

import com.citaa.citaa.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {
}
