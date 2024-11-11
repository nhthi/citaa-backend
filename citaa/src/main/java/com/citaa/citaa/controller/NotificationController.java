package com.citaa.citaa.controller;

import com.citaa.citaa.model.Notification;
import com.citaa.citaa.service.NotificationService;
import com.citaa.citaa.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/notification")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<Notification> createNotificaiton(@RequestBody Notification notification, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(notificationService.createNotification(notification), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(notificationService.getNotificationsByUserId(userService.findByJwt(jwt).getId(),false),HttpStatus.OK);
    }
    @PutMapping("/{notifi-id}")
    public ResponseEntity<Notification> readNotifications(@RequestHeader("Authorization") String jwt, @PathVariable("notifi-id") int notifiId) throws Exception {
        return new ResponseEntity<>(notificationService.readNotification(notifiId,userService.findByJwt(jwt).getId()),HttpStatus.OK);
    }

}
