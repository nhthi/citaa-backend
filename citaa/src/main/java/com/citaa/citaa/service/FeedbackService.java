package com.citaa.citaa.service;

import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.FeedbackRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.FeedbackCreationRequest;
import com.citaa.citaa.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;

    public Feedback createFeedback(FeedbackCreationRequest request) throws Exception {
        return feedbackRepository.save(Feedback.builder()
                        .topic(request.getTopic())
                        .heading(request.getHeading())
                        .fullName(request.getFullName())
                        .email(request.getEmail())
                        .createdAt(LocalDateTime.now())
                        .content(request.getContent())
                        .address(request.getAddress())
                        .status("None")
                .build());
    }

    public List<Feedback> getListFeedback(){
        return feedbackRepository.findAll();
    }

    public Feedback findFeedbackById(int id) throws Exception {
        Optional<Feedback> otp = feedbackRepository.findById(id);
        if(otp.isEmpty()){
            throw new Exception("Feedback not found");
        }
        return otp.get();
    }

    public Feedback replyFeedback(User admin, int feedback_id, String reply_content) throws Exception {
        Feedback feedback = findFeedbackById(feedback_id);
        String email = feedback.getEmail();
        String subject = "Trả lời phản hồi từ Citaa";

        if(feedback.getStatus().equals("Replied")){
            throw new Exception("This feedback has already been replied.");
        }

        if(reply_content == null || reply_content.isEmpty() || reply_content.trim().isEmpty()){
            throw new Exception("Reply content is empty");
        }
        String htmlContent =
               "<!DOCTYPE html>\n" +
                       "<html lang=\"en\">\n" +
                       "<head>\n" +
                       "    <meta charset=\"UTF-8\">\n" +
                       "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                       "    <title>Trả lời phản hồi của bạn</title>\n" +
                       "    <style>\n" +
                       "        body { \n" +
                       "            font-family: Arial, sans-serif; \n" +
                       "            background-color: #f4f4f4; \n" +
                       "            margin: 0; \n" +
                       "            padding: 0; \n" +
                       "        }\n" +
                       "        .email-container { \n" +
                       "            max-width: 600px; \n" +
                       "            margin: 0 auto; \n" +
                       "            background-color: #ffffff; \n" +
                       "            padding: 20px; \n" +
                       "            border-radius: 10px; \n" +
                       "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); \n" +
                       "        }\n" +
                       "        .header { \n" +
                       "            text-align: center; \n" +
                       "            padding-bottom: 20px; \n" +
                       "        }\n" +
                       "        .header img { \n" +
                       "            width: 100px; \n" +
                       "            border-radius: 50%; \n" +
                       "        }\n" +
                       "        .header h1 { \n" +
                       "            color: #333333; \n" +
                       "            font-size: 24px; \n" +
                       "            margin: 20px 0 10px; \n" +
                       "        }\n" +
                       "        .content { \n" +
                       "            color: #666666; \n" +
                       "            font-size: 16px; \n" +
                       "            line-height: 1.6; \n" +
                       "        }\n" +
                       "        .question { \n" +
                       "            background-color: #f0f0f0; \n" +
                       "            padding: 15px; \n" +
                       "            border-left: 4px solid #007BFF; \n" +
                       "            margin: 20px 0; \n" +
                       "        }\n" +
                       "        .response { \n" +
                       "            background-color: #e9ffe8; \n" +
                       "            padding: 15px; \n" +
                       "            border-left: 4px solid #28a745; \n" +
                       "            margin: 20px 0; \n" +
                       "        }\n" +
                       "        .footer { \n" +
                       "            text-align: center; \n" +
                       "            padding-top: 20px; \n" +
                       "            color: #999999; \n" +
                       "            font-size: 14px; \n" +
                       "        }\n" +
                       "        .footer a { \n" +
                       "            color: #007BFF; \n" +
                       "            text-decoration: none; \n" +
                       "        }\n" +
                       "    </style>\n" +
                       "</head>\n" +
                       "<body>\n" +
                       "    <div class=\"email-container\">\n" +
                       "        <div class=\"header\">\n" +
                       "            <img src=\"http://res.cloudinary.com/dssku7owl/image/upload/v1723724928/xs5rgk782juzntxm7zfy.png\" alt=\"Company Logo\">\n" +
                       "            <h1>Trả lời phản hồi của bạn</h1>\n" +
                       "        </div>\n" +
                       "        <div class=\"content\">\n" +
                       "            <p>Xin chào,</p>\n" +
                       "            <p>Cảm ơn đã kết nối đến chúng tôi để hiểu rõ nhau:</p>\n" +
                       "            <div class=\"question\">\n" +
                       "                <strong>Phản hồi của bạn:</strong><br>\n" +
                       "                <p>"+feedback.getContent()+"</p>\n" +
                       "            </div>\n" +
                       "            <div class=\"response\">\n" +
                       "                <strong>Câu trả lời từ chúng tôi:</strong><br>\n" +
                       "                <p>"+reply_content+"</p>\n" +
                       "            </div>\n" +
                       "            <p>Nếu bạn có câu hỏi khác hãy cho chúng tôi biết để phản hồi đến bạn</p>\n" +
                       "        </div>\n" +
                       "        <div class=\"footer\">\n" +
                       "            <p>&copy; 2024 Citaa.</p>\n" +
                       "        </div>\n" +
                       "    </div>\n" +
                       "</body>\n" +
                       "</html>\n";
        emailService.sendHtmlEmail(email, subject, htmlContent);
        feedback.setAdminReply(admin);
        feedback.setReplyContent(reply_content);
        feedback.setReplyAt(LocalDateTime.now());
        feedback.setStatus("Replied");
        return feedbackRepository.save(feedback);
    }

    public Page<Feedback> filterFeedbackAdmin( int year, String status, int pageNumber, int pageSize){

        List<Feedback> feedbacks = feedbackRepository.filterFeedback(year,status);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), feedbacks.size());

        List<Feedback> pageContent = feedbacks.subList(startIndex, endIndex);
        Page<Feedback> feedbackFilter = new PageImpl<>(pageContent, pageable, feedbacks.size());
        return feedbackFilter;
    }

    public ApiResponse deleteFeedbackById(int id) throws Exception {
        ApiResponse res = new ApiResponse();
        Feedback feedback = findFeedbackById(id);
        feedbackRepository.delete(feedback);
        res.setMessage("Xóa thành công");
        res.setStatus(200);
        return res;
    }
}
