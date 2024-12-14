package com.citaa.citaa.repository;

import com.citaa.citaa.model.Verify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyRepository extends JpaRepository<Verify, Long> {
    public Verify findByEmail(String email);
}

