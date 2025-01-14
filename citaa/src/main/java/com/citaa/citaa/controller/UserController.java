package com.citaa.citaa.controller;

import com.citaa.citaa.model.ConnectionRequest;
import com.citaa.citaa.model.Investor;
import com.citaa.citaa.model.React;
import com.citaa.citaa.model.User;
import com.citaa.citaa.request.UpdateUserRequest;
import com.citaa.citaa.response.EvaluationManagementResponse;
import com.citaa.citaa.response.MessageResponse;
import com.citaa.citaa.response.ProfileResponse;
import com.citaa.citaa.service.ConnectionService;
import com.citaa.citaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ConnectionService connectionService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfileById(@PathVariable("id") int id) throws Exception {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String jwt, @RequestBody UpdateUserRequest req) throws Exception {
        User user = userService.updateUser(jwt, req);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/expert/evaluation-management/{expertId}")
    public ResponseEntity<List<EvaluationManagementResponse>> getEvaluationManagement(@PathVariable("expertId") int expertId) throws Exception {
        return new ResponseEntity<>(userService.getEvaluateManagement(expertId), HttpStatus.OK);
    }

    @GetMapping("/top5-investor")
    public ResponseEntity<List<User>> getTop5Investors() throws Exception {
        return new ResponseEntity<>(userService.getTop5Investor(), HttpStatus.OK);
    }

    @GetMapping("/top5-profile")
    public ResponseEntity<List<User>> getTopProfile(@RequestParam("role") String role) throws Exception {
        return new ResponseEntity<>(userService.getTopProfile(role), HttpStatus.OK);
    }

    @GetMapping("/startup/count")
    public long countStartup() {
        return userService.countStartup();
    }

    @GetMapping("/expert/count")
    public long countExpert() {
        return userService.countExpert();
    }

    @GetMapping("/investor/count")
    public long countInvestor() {
        return userService.countInvestor();
    }

    @GetMapping("/profile/statistical/{userId}")
    public ResponseEntity<ProfileResponse> getProfileResponse(@PathVariable int userId) throws Exception {
        return new ResponseEntity<>(userService.getProfileStatistical(userId), HttpStatus.OK);
    }

    @GetMapping("/react")
    public ResponseEntity<List<React>> getReactUser(@RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(userService.findReactByJwt(jwt), HttpStatus.OK);
    }

    @GetMapping("/request")
    public ResponseEntity<Page<ConnectionRequest>> getConnectRequestByInvestor(@RequestHeader("Authorization") String jwt,
                                                                               @RequestParam(defaultValue = "0") String status,
                                                                               @RequestParam(defaultValue = "0") int year,
                                                                               @RequestParam(required = false) List<String> fields,
                                                                               @RequestParam(defaultValue = "1") int pageSize,@RequestParam(defaultValue = "0") int pageNumber) throws Exception {
        return new ResponseEntity<>(connectionService.getConnectionRequestsByInvestor(jwt,status,year,pageSize,pageNumber,fields),HttpStatus.OK);
    }


    @GetMapping("/startup/request")
    public ResponseEntity<Page<ConnectionRequest>> getConnectRequestByStartup(@RequestParam int userId,
                                                                               @RequestParam(defaultValue = "0") String status,
                                                                               @RequestParam(defaultValue = "1") int pageSize,@RequestParam(defaultValue = "0") int pageNumber) throws Exception {
        return new ResponseEntity<>(connectionService.getConnectionRequestsByStartup(userId,status,pageSize,pageNumber),HttpStatus.OK);
    }

    @PutMapping("/request-valid")
    public ResponseEntity<MessageResponse> requestValidUser(@RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(userService.sendRequestValidUser(jwt),HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<User>> filterUser(@RequestParam(defaultValue = "0") String province,
                                                 @RequestParam(defaultValue = "0") String fieldFilter,
                                                 @RequestParam(defaultValue = "0") String query,
                                                 @RequestParam(defaultValue = "0") String valid,
                                                 @RequestParam(defaultValue = "ROLE_STARTUP") String role,
                                                                              @RequestParam(defaultValue = "1") int pageSize,@RequestParam(defaultValue = "0") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.findByProvinceAndFieldAndQuery(province,role,fieldFilter,query,valid,pageSize,pageNumber),HttpStatus.OK);
    }

}
