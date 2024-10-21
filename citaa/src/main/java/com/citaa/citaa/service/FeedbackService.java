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

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    UserRepository userRepository;
    public Feedback createFeedback(FeedbackCreationRequest request) throws Exception {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new Exception("User not found!"));
        return feedbackRepository.save(Feedback.builder()
                        .field(request.getField())
                        .title(request.getTitle())
                        .name(request.getName())
                        .email(request.getEmail())
                        .user(user)
                .build());
    }

    public List<Feedback> getListFeedback(){
        return feedbackRepository.findAll();
    }
}
