package com.citaa.citaa.repository;

import com.citaa.citaa.model.ConnectionRequest;
import com.citaa.citaa.model.Project;
import org.springframework.data.domain.Pageable;
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


    @Query("SELECT cr.investor AS investor, COUNT(cr) AS connectionCount " +
            "FROM ConnectionRequest cr " +
            "WHERE cr.status = 'ACCEPT' " +
            "GROUP BY cr.investor " +
            "ORDER BY COUNT(cr) DESC")
    List<Object[]> findTop5InvestorsWithAcceptedConnections();

    @Query("SELECT cr.project, COUNT(cr.id) AS connectionCount " +
            "FROM ConnectionRequest cr where (:year = 0 or YEAR(cr.project.createAt) = :year) AND (:month = 0 or MONTH(cr.project.createAt) = :month)" +
            "GROUP BY cr.project " +
            "ORDER BY connectionCount DESC")
    List<Object[]> findTopProjectsWithConnections(Pageable pageable,int year,int month);

    @Query("SELECT cr.investor, COUNT(cr.id) AS connectionCount " +
            "FROM ConnectionRequest cr where cr.status = 'ACCEPT' and (:year = 0 or YEAR(cr.requestDate) = :year) AND (:month = 0 or MONTH(cr.requestDate) = :month)  " +
            "GROUP BY cr.project " +
            "ORDER BY connectionCount DESC")
    List<Object[]> findTopInvestorWithConnections(int year, int month);

    @Query("SELECT COUNT(DISTINCT c.project) " +
            "FROM ConnectionRequest c where (:year = 0 or YEAR(c.requestDate) = :year) AND (:month = 0 or MONTH(c.requestDate) = :month) " +
            "and c.status = 'ACCEPT'")
    long countProjectsWithAcceptedConnections(int year, int month);
}
