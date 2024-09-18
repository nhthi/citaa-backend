package com.citaa.citaa.service;


import com.citaa.citaa.model.Chat;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    @Autowired
    private UserService userService;
    @Autowired
    private ChatRepository chatRepository;

    public Chat createChat(User reqUser, User user) {
        Chat isExistChat = chatRepository.findChatByUsersId(user,reqUser);
        if(isExistChat != null){
            System.out.println("chat already exist");
            return isExistChat;
        }
        Chat chat = new Chat();
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        return chatRepository.save(chat);
    }

    public Chat findChatById(Integer id) throws Exception {
        Optional<Chat> opt = chatRepository.findById(id);
        if(opt.isEmpty()){
            throw new Exception("Chat not found with id: "+id);
        }
        return opt.get();
    }

    public List<Chat> findUsersChat(Integer userId) {
        return chatRepository.findByUsersId(userId);
    }
}
