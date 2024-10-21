package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.ExpertRepository;
import com.citaa.citaa.repository.InvestorRepository;
import com.citaa.citaa.repository.StartupRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.LoginRequest;
import com.citaa.citaa.response.AuthResponse;
import com.citaa.citaa.service.CustomerUserDetailService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;

@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {
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
    InvestorRepository investorRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isUsernameExist = userRepository.findByUsername(user.getAccount().getUsername());

        if(isUsernameExist != null){
            throw  new Exception("Username is already used with another account");
        }
        User createUser ;
        if(user.getAccount().getRole().equals("ROLE_STARTUP") ){
            createUser = new Startup();
            createUser = startupRepository.save(Startup.builder()
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .account(Account.builder()
                            .password(passwordEncoder.encode(user.getAccount().getPassword()))
                            .username(user.getAccount().getUsername())
                            .role(user.getAccount().getRole())
                            .createAt(LocalDateTime.now())
                            .status("disable")
                            .build())
                    .fields(user.getFields())
                    .build());
        }else if(user.getAccount().getRole().equals("ROLE_INVESTOR") ){
            createUser = new Investor();
            createUser = investorRepository.save(Investor.builder()
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .account(Account.builder()
                            .password(passwordEncoder.encode(user.getAccount().getPassword()))
                            .username(user.getAccount().getUsername())
                            .role(user.getAccount().getRole())
                            .createAt(LocalDateTime.now())
                            .status("disable")
                            .build())
                    .fields(user.getFields())
                    .investmentAmount(0.0)
                    .build());
        }else{
            createUser = new Expert();
            createUser = expertRepository.save(Expert.builder()
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .account(Account.builder()
                            .password(passwordEncoder.encode(user.getAccount().getPassword()))
                            .username(user.getAccount().getUsername())
                            .role(user.getAccount().getRole())
                            .createAt(LocalDateTime.now())
                            .status("disable")
                            .build())
                    .fields(user.getFields())
                    .build());
        }


        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getAccount().getUsername(), user.getAccount().getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        return new ResponseEntity<>(AuthResponse.builder()
                .jwt(jwt)
                .message("Register Success")
                .role(createUser.getAccount().getRole())
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req) throws Exception {
        String username = req.getUsername();
        String password = req.getPassword();

        Authentication authentication = authenticate(username,password);

        Collection<?extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        return new ResponseEntity<>(AuthResponse.builder()
                .jwt(jwt)
                .message("Sign in Success")
                .role(role)
                .build(), HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        UserDetails userDetails = customerUserDetailService.loadUserByUsername(username);
        if(userDetails==null){
            throw new Exception("Invalid Username ...");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new Exception("Invalid password ..");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


}
