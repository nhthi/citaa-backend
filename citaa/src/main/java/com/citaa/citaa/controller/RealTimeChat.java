package com.citaa.citaa.controller;

import com.citaa.citaa.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeChat {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Xử lý tin nhắn gửi tới nhóm
    @MessageMapping("/chat/group/{groupId}")
    public void sendToGroup(@Payload Message message,
                            @DestinationVariable String groupId) {
        // Gửi tin nhắn tới tất cả các user trong group
        simpMessagingTemplate.convertAndSend("/group/" + groupId, message);
    }

    // Xử lý tin nhắn gửi tới user cụ thể (private)
    @MessageMapping("/chat/private/{username}")
    public void sendToUser(@Payload Message message,
                           @DestinationVariable String username) {
        // Gửi tin nhắn tới user cụ thể
        simpMessagingTemplate.convertAndSendToUser(username, "/queue/private", message);
    }
}