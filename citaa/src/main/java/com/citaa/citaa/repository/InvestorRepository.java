package com.citaa.citaa.repository;

import com.citaa.citaa.model.Investor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Integer> {

    List<Investor> findTop5ByOrderByInvestmentAmountDesc();
}
