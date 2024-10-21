package com.citaa.citaa.controller;

import com.citaa.citaa.model.Investor;
import com.citaa.citaa.model.User;
import com.citaa.citaa.request.UpdateUserRequest;
import com.citaa.citaa.response.EvaluationManagementResponse;
import com.citaa.citaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

//    @GetMapping
//    public ResponseEntity<List<User>> getUsers(){
//        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
//    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfileById(@PathVariable("id") int id) throws Exception {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization")String jwt, @RequestBody UpdateUserRequest req) throws Exception {
        User user = userService.updateUser(jwt,req);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/expert/evaluation-management/{expertId}")
    public ResponseEntity<List<EvaluationManagementResponse>> getEvaluationManagement(@PathVariable("expertId") int expertId) throws Exception {
        return new ResponseEntity<>(userService.getEvaluateManagement(expertId),HttpStatus.OK);
    }

    @GetMapping("/top5-investor")
    public ResponseEntity<List<Investor>> getTop5Investors() throws Exception {
        return new ResponseEntity<>(userService.getTop5Investor(),HttpStatus.OK);
    }
}
