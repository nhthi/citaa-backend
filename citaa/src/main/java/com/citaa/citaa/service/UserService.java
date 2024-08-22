//package com.citaa.citaa.service;
//
//import com.citaa.citaa.model.Account;
//import com.citaa.citaa.model.Role;
//import com.citaa.citaa.model.User;
//import com.citaa.citaa.repository.UserRepository;
//import com.citaa.citaa.request.RequestUser;
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class UserService {
//    @Autowired
//    UserRepository userRepository;
//
//    public List<User> getUsers(){
//        return userRepository.findAll();
//    }
//
////    public User createUser(RequestUser request) throws Exception {
////
////        User existUser = userRepository.findUserByUsername(request.getUsername());
////
////        if(existUser != null){
////            throw new Exception("User is existing with username: "+request.getUsername());
////        }
////
////        Account account = Account.builder()
////                .username(request.getUsername())
////                .password(request.getPassword())
////                .role(Role.STARTUP.name())
////                .build();
////
////
////        User createUser = User.builder()
////                .fullName(request.getName())
////                .account(account)
////                .dob(request.getDob())
////                .address(request.getAddress())
////                .email(request.getEmail())
////                .valid(false)
////                .build();
////        ;
//
////        return userRepository.save(createUser);
//    }
//
//
//
//}
