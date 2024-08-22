//package com.citaa.citaa.controller;
//
//import com.citaa.citaa.model.User;
//import com.citaa.citaa.request.RequestUser;
//import com.citaa.citaa.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//    @Autowired
//    UserService userService;
//
//    @GetMapping
//    public ResponseEntity<List<User>> getUsers(){
//        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
//    }
//
//    @PostMapping
//    public String createUser (@RequestBody RequestUser req) throws Exception {
//        userService.createUser(req);
//        return "create success!";
//    }
//}
