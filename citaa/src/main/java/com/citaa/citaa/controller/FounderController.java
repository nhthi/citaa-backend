package com.citaa.citaa.controller;

import com.citaa.citaa.model.Founder;
import com.citaa.citaa.service.FounderService;
import com.citaa.citaa.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api/founder")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FounderController {
    @Autowired
    FounderService founderService;
    @Autowired
    UserService userService;
    @PostMapping
    public ResponseEntity<Founder> createFounder(@RequestBody Founder founder) throws Exception {
        return new ResponseEntity<>(founderService.createFounder(founder), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Founder>> getFounders(){
        return new ResponseEntity<>(founderService.getFounders(), HttpStatus.OK);
    }
}
