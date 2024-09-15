package com.citaa.citaa.service;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.Account;
import com.citaa.citaa.model.Role;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.RequestUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getProfile(String jwt) throws Exception{
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new Exception("User not found");
        }
        return user;
    }

    public User findById(int id) throws Exception{
        User user = userRepository.findById(id)
                .orElseThrow(()-> new Exception("User not found!"));
        if(user == null){
            throw new Exception("User not found");
        }
        return user;
    }



}
