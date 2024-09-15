package com.citaa.citaa.service;

import com.citaa.citaa.model.Chat;
import com.citaa.citaa.model.Message;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.ChatRepository;
import com.citaa.citaa.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;
    @Autowired
    ChatRepository chatRepository;
    public Message createMessage(User user, Integer chatId, Message req) throws Exception {
        Message message = new Message();
        Chat chat = chatService.findChatById(chatId);
        if(!chat.getUsers().contains(user)) {
            throw new Exception("You do not own this chat");
        }
        message.setUser(user);
        message.setFiles(req.getFiles());
        message.setContent(req.getContent());
        message.setCreateAt(LocalDateTime.now());
        chat.getMessages().add(message);
//        chatRepository.save(chat);
        return messageRepository.save(message);
    }
    public List<Message> findChatsMessages(Integer chatId) throws Exception {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(()-> new Exception("Chat not found with id!"));
        return chat.getMessages();
    }
}
