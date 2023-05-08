package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.config.jwt.JwtService;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.AlreadyExistException;
import com.example.gadgetariumb8.db.exception.exceptions.BadCredentialException;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.model.UserInfo;
import com.example.gadgetariumb8.db.model.enums.Role;
import com.example.gadgetariumb8.db.repository.UserInfoRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.AuthenticationService;
import com.example.gadgetariumb8.db.dto.request.AuthenticateRequest;
import com.example.gadgetariumb8.db.dto.request.RegisterRequest;
import com.example.gadgetariumb8.db.dto.response.AuthenticationResponse;
import com.example.gadgetariumb8.db.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.webjars.NotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userInfoRepository.existsByEmail(request.email())) {
            log.error(String.format("User with email %s is already exists", request.email()));
            throw new AlreadyExistException(String.format("User with email %s is already exists", request.email()));
        }
        String split = request.email().split("@")[0];
        if (split.equals(request.password())) {
            throw new BadRequestException("Create a stronger password");
        }
        UserInfo userInfo = UserInfo.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .userInfo(userInfo)
                .build();
        userRepository.save(user);
        log.info(String.format("User %s successfully saved!", userInfo.getEmail()));
        String token = jwtService.generateToken(userInfo);

        return AuthenticationResponse.builder()
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticateRequest request) {

        UserInfo userInfo = userInfoRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.error(String.format("User with email %s does not exists", request.email()));
                    throw new BadCredentialException(String.format("User with email %s does not exists", request.email()));
                });
        if (!passwordEncoder.matches(request.password(), userInfo.getPassword())) {
            log.error("Password does not match");
            throw new BadRequestException("Password does not match");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        log.info(String.format("User %s authenticated successfully", userInfo.getEmail()));
        String token = jwtService.generateToken(userInfo);

        return AuthenticationResponse.builder()
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
                .token(token)
                .build();
    }

    @Override
    public SimpleResponse forgotPassword(String email) {
        UserInfo userInfo = userRepository.findUserInfoByEmail(email)
                .orElseThrow(() -> new NotFoundException("User was not found"));
        String token = UUID.randomUUID().toString();
        userInfo.setResetPasswordToken(token);

        String subject = "Password Reset Request";
        String resetPasswordLink = "http://localhost:8080/reset-password?token=" + token;

        Context context = new Context();
        context.setVariable("title", "Password Reset");
        context.setVariable("message", "Please click link below for password reset!");
        context.setVariable("token", resetPasswordLink);
        context.setVariable("tokenTitle", "Reset Password");

        String htmlContent = templateEngine.process("reset-password-template.html", context);
        emailService.sendEmail(email, subject, htmlContent);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("The password reset was sent to your email. Please check your email.")
                .build();
    }

    @Override
    public SimpleResponse resetPassword(String token, String newPassword) {
        UserInfo userInfo = userInfoRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new NotFoundException("User was not found"));

        userInfo.setPassword(passwordEncoder.encode(newPassword));
        userInfo.setResetPasswordToken(null);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("User password changed successfully!")
                .build();
    }
}
