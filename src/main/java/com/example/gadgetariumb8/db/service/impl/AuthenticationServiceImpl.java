package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.config.jwt.JwtService;
import com.example.gadgetariumb8.db.dto.request.ForgotPasswordRequest;
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
            log.error(String.format("Пользователь с адресом электронной почты %s уже существует", request.email()));
            throw new AlreadyExistException(String.format("Пользователь с адресом электронной почты %s уже существует", request.email()));
        }
        String split = request.email().split("@")[0];
        if (split.equals(request.password())) {
            throw new BadRequestException("Создайте более надежный пароль");
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
        log.info(String.format("Пользователь %s успешно сохранен!", userInfo.getEmail()));
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
                    log.error(String.format("Пользователь с адресом электронной почты %s не существует", request.email()));
                    throw new BadCredentialException(String.format("Пользователь с адресом электронной почты %s не существует", request.email()));
                });
        if (!passwordEncoder.matches(request.password(), userInfo.getPassword())) {
            log.error("Пароль не подходит");
            throw new BadRequestException("Пароль не подходит");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        log.info(String.format("Пользователь %s успешно аутентифицирован", userInfo.getEmail()));
        String token = jwtService.generateToken(userInfo);

        return AuthenticationResponse.builder()
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
                .token(token)
                .build();
    }

    @Override
    public SimpleResponse forgotPassword(ForgotPasswordRequest request) {
        log.info("Updating reset password token for email: {}",request.email());
        UserInfo userInfo = userRepository.findUserInfoByEmail(request.email())
                .orElseThrow(() -> {
                    log.error(String.format("Пользователь с адресом электронной почты %s не существует", request.email()));
                    throw new NotFoundException(String.format("Пользователь с адресом электронной почты %s не существует", request.email()));
                });
        String token = UUID.randomUUID().toString();
        userInfo.setResetPasswordToken(token);

        String subject = "Password Reset Request";
        String resetPasswordLink = request.linkResetPassword() + "/" + token;

        Context context = new Context();
        context.setVariable("title", "Восстановление пароля");
        context.setVariable("message", "Пожалуйста, нажмите на ссылку ниже для сброса пароля!");
        context.setVariable("token", resetPasswordLink);
        context.setVariable("tokenTitle", "Восстановление пароля");

        String htmlContent = templateEngine.process("reset-password-template.html", context);

        emailService.sendEmail(request.email(), subject, htmlContent);
        log.info("Password reset email sent to: {}",request.email());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Сброс пароля был отправлен на вашу электронную почту. Пожалуйста, проверьте свою электронную почту.")
                .build();
    }

    @Override
    public SimpleResponse resetPassword(String token, String newPassword) {
        UserInfo userInfo = userInfoRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> {
                    log.error("Пользователь не существует");
                    throw new NotFoundException("User does not exists");
                });

        userInfo.setPassword(passwordEncoder.encode(newPassword));
        userInfo.setResetPasswordToken(null);
        log.info("Пароль пользователя успешно изменен!");
        log.info("User password updated successfully for user: {}", userInfo.getId());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Пароль пользователя успешно изменен!")
                .build();
    }
}
