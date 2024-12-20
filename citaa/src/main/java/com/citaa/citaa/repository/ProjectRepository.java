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

    @Query("select p from Project p where ((:minCapital is null and :maxCapital is null) or (p.realTotalCapital between :minCapital and :maxCapital))" +
            "and  (:status = 'all' or p.valid= :valid) ")
    public List<Project> filterProjects( @Param("minCapital") double minCapital,
                                        @Param("maxCapital")double maxCapital,
                                        @Param("status") String status, @Param("valid")boolean valid );
    @Query("select p from Project p where p.avg > 0 ")
    public List<Project> getTopProjectPotential();

    @Query("select p from Project p where p.name like %:query% or " +
            "p.introduce like %:query% or p.startUpIdea like %:query% or p.field like %:query%" )
    public List<Project> searchProject(@Param("query") String query);

    @Query("SELECT p.startup, COUNT(p) as projectCount FROM Project p GROUP BY p.startup ORDER BY projectCount DESC")
    List<Object[]> findTopStartupsWithMostProjects();
}