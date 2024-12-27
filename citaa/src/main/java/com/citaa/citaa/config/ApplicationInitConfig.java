package com.citaa.citaa.config;

import com.citaa.citaa.model.Account;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.AccountRepository;
import com.citaa.citaa.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    @Bean
    ApplicationRunner applicationRunner(){

        return args -> {
            if(accountRepository.findByUsername("root") == null){
                Account account = Account.builder()
                        .username("root")
                        .password(passwordEncoder.encode("root"))
                        .status("enable")
                        .role("ROLE_ROOT")
                        .build();
                userRepository.save(User.builder()
                        .fullName("ROOT")
                        .phone("011111111")
                        .gender(true)
                        .email("adminlaptopstore@gmail.com")
                        .address("Hà Nội")
                        .avatar("https://cdn.pixabay.com/photo/2024/04/08/11/42/doggy-8683291_640.jpg")
                        .account(account)
                        .build());
                log.warn("Account admin has been created with password: admin");
            }
        };
    }
}
