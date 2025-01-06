package com.citaa.citaa.repository;

import com.citaa.citaa.model.Competition;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

    @Query("SELECT c FROM Competition c WHERE " +
            "(:year IS NULL OR YEAR(c.startAt) = :year) AND " +
            "(:field = 'all' OR :field MEMBER OF c.fields) AND " +
            "(:status = 'all' OR " +
            "(:status = 'ongoing' AND c.startAt <= CURRENT_DATE AND c.endAt >= CURRENT_DATE) OR " +
            "(:status = 'ended' AND c.endAt < CURRENT_DATE) OR " +
            "(:status = 'upcoming' AND c.startAt > CURRENT_DATE))")
    public List<Competition> filterCompetition(String year, String field, String status );

    @Query("select c from Competition c where (:year = 0 or YEAR(c.startAt) = :year)")
    public List<Competition> filterAllCompetition(int year);

    @Query("SELECT c FROM Competition c WHERE :status = 'all' or " +
            "((:status = 'ongoing' AND c.startAt <= CURRENT_DATE AND c.endAt >= CURRENT_DATE) OR " +
            "(:status = 'ended' AND c.endAt < CURRENT_DATE) OR " +
            "(:status = 'upcoming' AND c.startAt > CURRENT_DATE))")
    public List<Competition> filterCompetitionByStatus(String status );

    @Query("Select c from Competition c where (:field member of c.fields) and (:year = 0 or YEAR(c.createAt) = :year) AND (:month = 0 or MONTH(c.createAt) = :month)")
    public List<Competition> findByField(String field,int year,int month);

    @Query("Select c from Competition c where :fields not member of c.fields")
    public List<Competition> findByNotFields(String fields);

    @Query("SELECT c FROM Competition c where (:year = 0 or YEAR(c.createAt) = :year) AND (:month = 0 or MONTH(c.createAt) = :month) ORDER BY SIZE(c.projects) DESC")
    List<Competition> findTop3ByProjectsCount(Pageable pageable,int year,int month);

    List<Competition> findAllByProjectsContaining(Project project);

    @Query("SELECT COUNT(c) FROM Competition c JOIN c.startupAppliedTimes s WHERE KEY(s) = :startupId")
    Long countCompetitionsByStartupId(int startupId);


    @Query("SELECT COUNT(c) FROM Competition c JOIN c.judges j WHERE j.id = :userId")
    Long countJudgingCompetitionsByExpertId(int userId);


    @Query("SELECT COUNT(c) " +
            "FROM Competition c " +
            "WHERE (:year = 0 or YEAR(c.createAt) = :year) AND (:month = 0 or MONTH(c.createAt) = :month)")
    long countCompetitionByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT c FROM Competition c JOIN c.judges j WHERE j = :judge and" +
            "( :status = 'all' or" +
            "(:status = 'ongoing' AND c.startAt <= CURRENT_DATE AND c.endAt >= CURRENT_DATE) OR " +
            "(:status = 'ended' AND c.endAt < CURRENT_DATE) OR " +
            "(:status = 'upcoming' AND c.startAt > CURRENT_DATE))")
    List<Competition> filterByJudge(User judge, String status);

}
