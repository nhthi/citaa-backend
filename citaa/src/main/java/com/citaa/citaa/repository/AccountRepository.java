package com.citaa.citaa.repository;

import com.citaa.citaa.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    public Account findByUsername(String username);

    @Query("SELECT COUNT(c) " +
            "FROM Account c " +
            "WHERE (:year = 0 or YEAR(c.createAt) = :year) AND (:month = 0 or MONTH(c.createAt) = :month)")
    long countAccount(@Param("year") int year, @Param("month") int month);
}
