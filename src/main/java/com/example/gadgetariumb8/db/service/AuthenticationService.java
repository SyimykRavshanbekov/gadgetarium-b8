package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.AuthenticateRequest;
import com.example.gadgetariumb8.db.dto.request.RegisterRequest;
import com.example.gadgetariumb8.db.dto.response.AuthenticationResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticateRequest request);

    SimpleResponse forgotPassword(String email) throws MessagingException;

    SimpleResponse resetPassword(String token, String newPassword);
}
