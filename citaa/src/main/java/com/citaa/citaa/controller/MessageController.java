package com.citaa.citaa.controller;

import com.citaa.citaa.model.Message;
import com.citaa.citaa.model.User;
import com.citaa.citaa.service.ChatService;
import com.citaa.citaa.service.MessageService;
import com.citaa.citaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    @PostMapping("/api/message/chat/{chatId}")
    public Message createMessage(@RequestBody Message req,
                                 @RequestHeader("Authorization")String jwt,
                                 @PathVariable("chatId") Integer chatId) throws Exception {
        User user = userService.findByJwt(jwt);
        Message message = messageService.createMessage(user,chatId,req);
        return message;
    }

    @GetMapping("/api/message/chat/{chatId}")
    public List<Message> findChatsMessage(@RequestHeader("Authorization")String jwt,
                                          @PathVariable("chatId") Integer chatId) throws Exception {
        User user = userService.findByJwt(jwt);
        List<Message> messages= messageService.findChatsMessages(chatId);
        return messages;
    }
}
