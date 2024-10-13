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

}
