package com.citaa.citaa.repository;

import com.citaa.citaa.model.Founder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FounderRepository extends JpaRepository<Founder, Integer> {
}
