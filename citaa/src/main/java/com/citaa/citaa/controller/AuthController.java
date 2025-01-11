package com.citaa.citaa.controller;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.exception.AuthException;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.*;
import com.citaa.citaa.request.ChangePasswordRequest;
import com.citaa.citaa.request.LoginRequest;
import com.citaa.citaa.request.ResetPasswordRequest;
import com.citaa.citaa.response.ApiResponse;
import com.citaa.citaa.response.AuthResponse;
import com.citaa.citaa.response.MessageResponse;
import com.citaa.citaa.service.CustomerUserDetailService;
import com.citaa.citaa.service.EmailService;
import com.citaa.citaa.service.UserService;
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
import java.util.Random;

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
    @Autowired
    UserService userService;
    @Autowired
    VerifyRepository verifyRepository;
    @Autowired
    EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isUsernameExist = userRepository.findByUsername(user.getAccount().getUsername());

        if(isUsernameExist != null){
            throw  new AuthException("Tên đăng nhập đã tồn tại");
        }

        User isExistEmail = userRepository.findByEmail(user.getEmail());

        if(isExistEmail != null){
            throw  new AuthException("Email đã được đăng ký bởi tài khoản khác");
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
                            .status("enable")
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
                            .status("enable")
                            .build())
                    .fields(user.getFields())
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
                            .status("enable")
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
                .message("Đăng nhập thành công")
                .role(role)
                .build(), HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        UserDetails userDetails = customerUserDetailService.loadUserByUsername(username);
        if(userDetails==null){
            throw new AuthException("Tên đăng nhập hoặc mật khẩu không đúng");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new AuthException("Tên đăng nhập hoặc mật khẩu không đúng");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam("email") String email) throws Exception {
        User user = userService.findUserByEmail(email);
        String code = generateRandomNumber();
        Verify verify = verifyRepository.findByEmail(email);
        if (verify == null) {
            verify = new Verify();
            verify.setEmail(email);
        }
        verify.setVerifyCode(code);
        verifyRepository.save(verify);
        String subject = "Xác thực tài khoản";
        String htmlContent = "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Email xác thực</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }"
                + ".email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; padding-bottom: 20px; }"
                + ".header img { width: 100px; border-radius: 50%; }"
                + ".header h1 { color: #333333; font-size: 24px; margin: 20px 0 10px; }"
                + ".content { color: #666666; font-size: 16px; line-height: 1.6; }"
                + ".verification-code { background-color: #007BFF; color: #ffffff; font-size: 22px; font-weight: bold; text-align: center; padding: 15px; border-radius: 5px; margin: 20px 0; letter-spacing: 2px; }"
                + ".cta-button { display: inline-block; background-color: #28a745; color: #FFFFFF; padding: 5px 20px; text-align: center; text-decoration: none; border-radius: 5px; font-size: 15px; font-weight: bold; transition: background-color 0.3s ease; }"
                + ".footer { text-align: center; padding-top: 20px; color: #999999; font-size: 14px; }"
                + ".footer a { color: #007BFF; text-decoration: none; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='email-container'>"
                + "<div class='header'>"
                + "<img src='http://res.cloudinary.com/dssku7owl/image/upload/v1723724928/xs5rgk782juzntxm7zfy.png' alt='Company Logo'>"
                + "<h1>Email xác thực</h1>"
                + "</div>"
                + "<div class='content'>"
                + "<p>Chào bạn,</p>"
                + "<p>Cảm ơn đã liên hệ với chúng tôi! Đưới đây là mã xác thực để lấy lại mật khẩu của bạn:</p>"
                + "<div class='verification-code'>" + code + "</div>"
                + "<p>Nếu bạn chưa đăng ký tài khoản này, vui lòng bỏ qua email này hoặc liên hệ với đội ngũ hỗ trợ của chúng tôi.</p>"
//                + "<a href='http://localhost:3000/account/forgotpassword?open=true' class='cta-button'>Verify Here</a>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>Nếu có câu hỏi khác, <a href='http://localhost:3000/contact'>liên hệ ngay với chúng tôi</a> để được hỗ trợ nhanh chóng.</p>"
                + "<p>&copy; 2024 Citaa.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        emailService.sendHtmlEmail(email, subject, htmlContent);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Mã OTP đã được gửi đến email của bạn");
        messageResponse.setStatus(200);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest req) throws Exception {
        Verify verify = verifyRepository.findByEmail(req.getEmail());
        MessageResponse res = new MessageResponse();
        if (verify != null) {
            if (verify.getVerifyCode().equals(req.getCode())) {
                res.setMessage(UpdatePassword(req.getEmail(), req.getNewPassword()));
                res.setStatus(200);
                verifyRepository.delete(verify);
            } else {
                res.setMessage("Mã OTP không chính xác");
                res.setStatus(400);
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public String UpdatePassword(String email, String newPassword) throws Exception {
        User user = userService.findUserByEmail(email);
        user.getAccount().setPassword(passwordEncoder.encode(newPassword));
        return "Đổi mật khẩu thành công";
    }

    @PutMapping("/change-password")
    public ApiResponse changePassword(@RequestHeader("Authorization")String jwt, @RequestBody ChangePasswordRequest req) throws Exception {
        ApiResponse res = new ApiResponse();
        return userService.changePassword(jwt,req.getCurrentPassword(), req.getNewPassword());
    }

    public static String generateRandomNumber() {
        Random random = new Random();
        int number = 1000 + random.nextInt(9000);
        return String.valueOf(number);
    }
}
