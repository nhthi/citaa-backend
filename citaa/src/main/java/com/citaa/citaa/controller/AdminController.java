package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.Expert;
import com.citaa.citaa.model.Investor;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.repository.ExpertRepository;
import com.citaa.citaa.repository.InvestorRepository;
import com.citaa.citaa.repository.StartupRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.service.CustomerUserDetailService;
import com.citaa.citaa.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/api")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CustomerUserDetailService customerUserDetailService;
    @Autowired
    StartupRepository startupRepository;
    @Autowired
    ExpertRepository expertRepository;
    @Autowired
    UserService userService;

    @PostMapping("/add-to-expert/{expertId}/{projectId}")
    public ResponseEntity<Expert> addProjectToExpert( @PathVariable int expertId, @PathVariable int projectId) throws Exception {
        return new ResponseEntity<>(userService.addProjectToExpert(projectId,expertId), HttpStatus.CREATED);
    }
    @GetMapping("/all-startup")
    public ResponseEntity<Page<Startup>> getAllStartup(@RequestParam("pageSize") int pageSize,
                                                       @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllStartups(pageSize,pageNumber),HttpStatus.OK);
    }

    @GetMapping("/all-expert")
    public ResponseEntity<Page<Expert>> getAllExpert(@RequestParam("pageSize") int pageSize,
                                                       @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllExpert(pageSize,pageNumber),HttpStatus.OK);
    }

    @GetMapping("/all-investor")
    public ResponseEntity<Page<Investor>> getAllInvestor(@RequestParam("pageSize") int pageSize,
                                                        @RequestParam("pageNumber") int pageNumber) throws Exception {
        return new ResponseEntity<>(userService.getAllInvestor(pageSize,pageNumber),HttpStatus.OK);
    }
}
