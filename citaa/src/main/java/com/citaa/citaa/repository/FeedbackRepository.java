package com.citaa.citaa.repository;

import com.citaa.citaa.model.Chat;
import com.citaa.citaa.model.Feedback;
import com.citaa.citaa.model.User;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("select f from Feedback f where (:year = 0 or year(f.createdAt)=:year) " +
            "and (:status = '0' or f.status = :status)")
    public List<Feedback> filterFeedback(int year, String status);

    @Query("SELECT COUNT(c) " +
            "FROM Feedback c " +
            "WHERE (:year = 0 or YEAR(c.createdAt) = :year) AND (:month = 0 or MONTH(c.createdAt) = :month)")
    long countFeedbacksByMonthAndYear(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(c) " +
            "FROM Feedback c " +
            "WHERE (:year = 0 or YEAR(c.createdAt) = :year) AND (:month = 0 or MONTH(c.createdAt) = :month) AND c.status = 'Replied' ")
    long countReplyFeedbacksByMonthAndYear(@Param("year") int year, @Param("month") int month);
}
