package com.citaa.citaa.controller;

import com.citaa.citaa.model.Chat;
import com.citaa.citaa.model.User;
import com.citaa.citaa.request.ChatCreationRequest;
import com.citaa.citaa.service.ChatService;
import com.citaa.citaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    @PostMapping("/api/chats")
    public Chat createChat(@RequestHeader("Authorization")String jwt, @RequestBody ChatCreationRequest req) throws Exception {
        User reqUser = userService.findByJwt(jwt);
        User user2 = userService.findById(req.getChatUserId());
        Chat chat = chatService.createChat(reqUser,user2);
        return chat;
    }

    @GetMapping("/api/chats")
    public List<Chat> findUsersChat(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findByJwt(jwt);
        return chatService.findUsersChat(user.getId());
    }
    
}
