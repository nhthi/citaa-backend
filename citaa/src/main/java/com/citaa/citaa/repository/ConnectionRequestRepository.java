package com.citaa.citaa.repository;

import com.citaa.citaa.model.ConnectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {

    @Query("select c from ConnectionRequest c where c.investor.id = :userId")
    List<ConnectionRequest> findByUserId(int userId);

}
