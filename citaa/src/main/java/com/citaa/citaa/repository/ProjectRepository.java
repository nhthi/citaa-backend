package com.citaa.citaa.repository;

import com.citaa.citaa.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p where p.startup.id =:startupId")
    public List<Project> findByStartupId(@Param("startupId") int startupId);

    @Query("select p from Project p where (:minCapital=0  or p.realTotalCapital >= :minCapital)" +
            "and  (:status = '0' or p.status= :status) " +
            "and (:year = 0 or YEAR(p.createAt) = :year)" +
            "and (:countExpert = -1 or (:countExpert = 3 and p.countExpert = 3) or (:countExpert = 2 and p.countExpert < 3))")
    public List<Project> filterProjects(@Param("minCapital") double minCapital,
                                        @Param("status") String status,
                                        @Param("year") int year,
                                        @Param("countExpert") int countExpert);

    @Query("select p from Project p where p.avg > 0 ")
    public List<Project> getTopProjectPotential();

    @Query("select p from Project p where p.name like %:query% or " +
            "p.introduce like %:query% or p.startUpIdea like %:query% or p.field like %:query%")
    public List<Project> searchProject(@Param("query") String query);

    @Query("SELECT p.startup, COUNT(p) as projectCount FROM Project p GROUP BY p.startup ORDER BY projectCount DESC")
    List<Object[]> findTopStartupsWithMostProjects();

    @Query("SELECT COUNT(DISTINCT e.id) FROM Project e WHERE e.startup.id = :startupId")
    Long countDistinctProjectByStartupId(int startupId);

    @Query("SELECT SUM(c.realTotalCapital) FROM Project c  WHERE c.startup.id = :startupId")
    Double sumTotalCapital(int startupId);
}