package com.citaa.citaa.repository;

import com.citaa.citaa.model.Expert;
import com.citaa.citaa.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {
}
