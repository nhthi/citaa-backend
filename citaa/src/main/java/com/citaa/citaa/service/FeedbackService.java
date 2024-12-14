package com.citaa.citaa.service;

import com.citaa.citaa.model.Feedback;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.FeedbackRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.FeedbackCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    UserRepository userRepository;
    public Feedback createFeedback(FeedbackCreationRequest request) throws Exception {
        return feedbackRepository.save(Feedback.builder()
                        .topic(request.getTopic())
                        .heading(request.getHeading())
                        .fullName(request.getFullName())
                        .email(request.getEmail())
                        .createdAt(LocalDateTime.now())
                        .content(request.getContent())
                        .address(request.getAddress())
                .build());
    }

    public List<Feedback> getListFeedback(){
        return feedbackRepository.findAll();
    }
}
