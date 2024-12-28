package com.citaa.citaa.repository;

import com.citaa.citaa.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT COUNT(c) " +
            "FROM Comment c " +
            "WHERE (:year = 0 or YEAR(c.createAt) = :year) AND (:month = 0 or MONTH(c.createAt) = :month)")
    long countCommentsByMonthAndYear(@Param("year") int year, @Param("month") int month);
}
