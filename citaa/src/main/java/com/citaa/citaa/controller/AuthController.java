package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.Account;
import com.citaa.citaa.model.User;
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
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        System.out.println(user.getAccount().getUsername());
        User isUsernameExist = userRepository.findByUsername(user.getAccount().getUsername());
        System.out.println(isUsernameExist);
        if(isUsernameExist != null){
            throw  new Exception("Username is already used with another account");
        }
        User createUser = new User();
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        account.setUsername(user.getAccount().getUsername());
        account.setRole(user.getAccount().getRole());
        account.setCreateAt(LocalDateTime.now());

        createUser.setEmail(user.getEmail());
        createUser.setFullName(user.getFullName());
        createUser.setAccount(account);

        User savedUser = userRepository.save(createUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getAccount().getUsername(), user.getAccount().getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authRespone = new AuthResponse();
        authRespone.setJwt(jwt);
        authRespone.setMessage("Register Success");
        authRespone.setRole(savedUser.getAccount().getRole());
        return new ResponseEntity<>(authRespone, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req) throws Exception {
        String username = req.getUsername();
        String password = req.getPassword();

        Authentication authentication = authenticate(username,password);

        Collection<?extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authRespone = new AuthResponse();
        authRespone.setJwt(jwt);
        authRespone.setMessage("Signin Success");

        authRespone.setRole(role);
        System.out.println("Role: "+role);
        return new ResponseEntity<>(authRespone, HttpStatus.OK);
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
