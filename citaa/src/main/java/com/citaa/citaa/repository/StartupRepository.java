package com.citaa.citaa.repository;

import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StartupRepository extends JpaRepository<Startup, Integer> {

    @Query("select s from Startup s where s.account.username=:username")
    public Startup findByUsername(@Param("username") String username);
}
