package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.config.jwt.JwtService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author kurstan
 * @created at 13.04.2023 10:01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userInfoRepository.existsByEmail(request.email())) {
            log.error(String.format("User with email %s is already exists", request.email()));
            throw new AlreadyExistException(String.format("User with email %s is already exists", request.email()));
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
        log.info(String.format("User %s is saved!", userInfo.getEmail()));
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
            .orElseThrow(()-> {
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

        String token = jwtService.generateToken(userInfo);

        return AuthenticationResponse.builder()
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
                .token(token)
                .build();
    }
}
