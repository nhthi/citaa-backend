package com.citaa.citaa.repository;

import com.citaa.citaa.model.ConnectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {

    @Query("select c from ConnectionRequest c where c.investor.id = :userId and (:status ='0' or c.status = :status) and (:year = 0 or YEAR(c.requestDate) = :year)")
    List<ConnectionRequest> filterConnections(int userId,String status,int year);


    @Query("select c from ConnectionRequest c where c.project.id = :projectId")
    List<ConnectionRequest> findByProjectId(int projectId);

    @Query("select c from ConnectionRequest c where (c.project.startup.id = :userId and (:status ='0' or c.status = :status))")
    List<ConnectionRequest> findByStartup(int userId,String status);

}
