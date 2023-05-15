package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ProfileImageRequest;
import com.example.gadgetariumb8.db.dto.request.ProfilePasswordResetRequest;
import com.example.gadgetariumb8.db.dto.request.ProfileRequest;
import com.example.gadgetariumb8.db.dto.response.ProfileResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public ProfileResponse updateUserDetails(ProfileRequest request) {
        User user = getAuthenticate();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.getUserInfo().setEmail(request.email());
        user.setAddress(request.address());
        userRepository.save(user);
        return new ProfileResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getUserInfo().getEmail(),
                user.getAddress());
    }

    @Override
    public SimpleResponse resetPassword(ProfilePasswordResetRequest request) {
        User user = getAuthenticate();
        if (encoder.matches(request.currentPassword(), user.getUserInfo().getPassword())) {
            user.getUserInfo().setPassword(encoder.encode(request.newPassword()));
            userRepository.save(user);
        } else {
            throw new BadRequestException("Password doesn't matched");
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Your password is successfully reset").build();
    }

    @Override
    public SimpleResponse setImage(ProfileImageRequest request) {
        getAuthenticate().setImage(request.imageUrl());
        return SimpleResponse.builder().message(String.format("Profile with id %s image updated successfully", getAuthenticate().getId())).build();
    }

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Token has been taken!");
        return userRepository.findUserByEmail(login).orElseThrow(() -> {
            log.error("User not found!");
            throw new NotFoundException("User not found!");
        });
    }
}
