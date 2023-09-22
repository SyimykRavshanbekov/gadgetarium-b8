package com.example.gadgetariumb8;

import com.example.gadgetariumb8.db.dto.request.ProfileImageRequest;
import com.example.gadgetariumb8.db.dto.request.ProfileRequest;
import com.example.gadgetariumb8.db.dto.response.ProfileResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.model.UserInfo;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateUserDetails_ValidRequest_SuccessfullyUpdatesUserDetails() {
//        ProfileRequest request = new ProfileRequest("Nuriza", "Muratova", "+996554488060", "mnuriza@gmail.com", "Grajdanskaya 119,Vostok-5,Bishkek");
//
//        User user = new User();
//        UserInfo userInfo = new UserInfo();
//        user.setUserInfo(userInfo);
//
//        ProfileResponse expectedResponse = new ProfileResponse(
//                "link",
//                "Nuriza",
//                "Muratova",
//                "+996554488060",
//                "mnuriza@gmail.com",
//                "Grajdanskaya 119,Vostok-5,Bishkek");
//
//        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken("mnuriza@gmail.com", "Nuriza2023");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        ProfileResponse response = profileService.updateUserDetails(request);
//        assertEquals(expectedResponse, response);
//
//        verify(userRepository, times(1)).save(user);

    }

    @Test
    void setImage_ValidRequest_SuccessfullySetsProfileImage() {
//        ProfileImageRequest request = new ProfileImageRequest("https://example.com/profile-image.jpg");
//
//        User authenticatedUser = new User();
//        authenticatedUser.setId(1L);
//
//        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(authenticatedUser));
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken("mnuriza9@gmail.com", "Nuriza2023");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        SimpleResponse expectedResponse = new SimpleResponse(HttpStatus.OK, "Profile with id 1 image updated successfully");
//        SimpleResponse response = profileService.setImage(request);
//
//        assertEquals(expectedResponse, response);
//        verify(userRepository, times(2)).findUserByEmail(anyString());
    }
}