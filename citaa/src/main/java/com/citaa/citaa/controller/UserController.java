package com.citaa.citaa.controller;

import com.citaa.citaa.model.Investor;
import com.citaa.citaa.model.React;
import com.citaa.citaa.model.User;
import com.citaa.citaa.request.UpdateUserRequest;
import com.citaa.citaa.response.EvaluationManagementResponse;
import com.citaa.citaa.response.ProfileResponse;
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
    public ResponseEntity<List<User>> getTop5Investors() throws Exception {
        return new ResponseEntity<>(userService.getTop5Investor(),HttpStatus.OK);
    }

    @GetMapping("/top5-profile")
    public ResponseEntity<List<User>> getTopProfile(@RequestParam("role") String role) throws Exception {
        return new ResponseEntity<>(userService.getTopProfile(role),HttpStatus.OK);
    }

    @GetMapping("/startup/count")
    public long countStartup(){
        return userService.countStartup();
    }

    @GetMapping("/expert/count")
    public long countExpert(){
        return userService.countExpert();
    }

    @GetMapping("/investor/count")
    public long countInvestor(){
        return userService.countInvestor();
    }

    @GetMapping("/profile/statistical/{userId}")
    public ResponseEntity<ProfileResponse> getProfileResponse(@PathVariable int userId) throws Exception {
        return new ResponseEntity<>(userService.getProfileStatistical(userId),HttpStatus.OK);
    }

    @GetMapping("/react")
    public ResponseEntity<List<React>> getReactUser(@RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(userService.findReactByJwt(jwt),HttpStatus.OK);
    }
}
