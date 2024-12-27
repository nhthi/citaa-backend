package com.citaa.citaa.repository;

import com.citaa.citaa.model.React;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactRepository extends JpaRepository<React, Integer> {

    public List<React> findAllByUserId(int userId);
}
