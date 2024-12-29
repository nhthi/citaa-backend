package com.citaa.citaa.controller;

import com.citaa.citaa.model.Message;
import com.citaa.citaa.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeChat {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeChat.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{groupId}")
    public Message sendToUser(@Payload Message message, @DestinationVariable String groupId) {
        logger.info("Sending message to user: {}", groupId);
        simpMessagingTemplate.convertAndSendToUser(groupId, "/private", message);
        return message;
    }
}